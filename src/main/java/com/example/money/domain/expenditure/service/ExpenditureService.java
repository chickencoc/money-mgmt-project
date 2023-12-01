package com.example.money.domain.expenditure.service;

import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import com.example.money.domain.budgetByCategory.repository.BudgetByCategoryRepository;
import com.example.money.domain.expenditure.dto.ExpenditureCreateRequestDto;
import com.example.money.domain.expenditure.dto.ExpenditureResponseDto;
import com.example.money.domain.expenditure.dto.ExpenditureSearchDto;
import com.example.money.domain.expenditure.dto.ExpenditureUpdateRequestDto;
import com.example.money.domain.expenditure.entity.Expenditure;
import com.example.money.domain.expenditure.repository.ExpenditureRepository;
import com.example.money.domain.member.entity.Member;
import com.example.money.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private MemberRepository memberRepository;
    private BudgetByCategoryRepository budgetByCategoryRepository;
    private ExpenditureRepository expenditureRepository;
    private EntityManager entityManager;

    /**
     * 사용자의 새로운 지출 목록을 생성합니다.
     * */
    @Transactional
    public ExpenditureResponseDto saveExpenditures(User user, ExpenditureCreateRequestDto request) {

        Member member = checkMember(user);

        BudgetByCategory budgetByCategory = budgetByCategoryRepository.findByCategoryIdAndBudgetId(request.getCategoryId(), request.getBudgetId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found BudgetByCategory"));

        // request를 이용해 expenditure 객체 생성
        Expenditure expenditure = request.toExpenditure(budgetByCategory, member);

        // expenditure 객체를 DB에 저장
        return ExpenditureResponseDto.from(expenditureRepository.save(expenditure));
    }

    /**
     * 사용자의 지출 목록을 수정합니다.
     * */
    @Transactional
    public ExpenditureResponseDto updateExpenditure(User user, ExpenditureUpdateRequestDto request) {

        Member member = checkMember(user);

        // request에 주어진 id 값으로 지출 데이터를 DB에서 조회
        Expenditure expenditure = expenditureRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found Expenditure"));

        // 지출 데이터 수정 todo: dirty check?
        expenditure.update(request);

        return ExpenditureResponseDto.from(expenditure);
    }

    /**
     * 사용자의 지출 목록을 조회합니다. 기본으로 월단위 목록이 조회 되며, 일자 또는 금액의 기준을 추가하여 목록을 조회할 수 있습니다.
     * 목록을 반환할 때 전체 합계와 카테고리별 합계를 제공합니다.
     * */
    public ExpenditureSearchDto.Response getExpendituresByCondition(User user, LocalDate startDate, LocalDate endDate, String categoryName, Integer minAmount, Integer maxAmount) {

        Member member = checkMember(user);

        // -- 동적 쿼리를 위한 JPQL

        String jpql = "select exp from Expenditure exp where exp.member = :member"; // 특정 사용자가 조회하기 때문에 member를 기본 조건으로 설정
        final String DATE_CONDITION = " and exp.usageTime between :startDate and :endDate"; // 조회 조건 시작일과 종료일을 조건
        final String CATEGORY_CONDITION = " and exp.budgetByCategory.category.name = :categoryName"; // 카테고리 이름으로 검색하기 위한 조건
        final String AMOUNT_CONDITION_PRIFIX = " and exp.amount";
        final String MINIMUM_AMOUNT = " >= :minAmount"; // 검색 조건의 금액 최솟값 조건
        final String MAXIMUM_AMOUNT = " <= :maxAmount"; // 검색 조건의 금액 최댓값 조건

        // 필요한 query parameter를 한번에 적용시키기 위해 map을 생성
        Map<String, Object> parameters = new HashMap<>();
        // JPQL에 일자 조건을 추가
        jpql += DATE_CONDITION;

        // 시작일과 종료일이 둘 중 하나라도 없을 경우, 지난 일주일을 조건으로 적용
        if(Objects.isNull(startDate) || Objects.isNull(endDate)) {
            LocalDate today = LocalDate.now();
            LocalDate pastWeek = today.minusWeeks(1L).plusDays(1L);

            parameters.put("startDate", pastWeek);
            parameters.put("endDate", today);
        } else {
            // 시작일과 종료일이 있을 경우, 해당 값을 조건으로 적용
            parameters.put("startDate", startDate);
            parameters.put("endDate", endDate);
        }

        // 카테고리 이름 값이 있을 경우, 조건 추가
        if(StringUtils.hasText(categoryName)) {
           jpql += CATEGORY_CONDITION;

           parameters.put("categoryName", categoryName);
        }

        // 금액 최솟값이 있을 경우, 조건 추가
        if(!Objects.isNull(minAmount)) {
            jpql += AMOUNT_CONDITION_PRIFIX + MINIMUM_AMOUNT;

            parameters.put("minAmount", minAmount);
        }

        // 금액 최댓값이 있을 경우, 조건 추가
        if(!Objects.isNull(maxAmount)) {
            jpql += AMOUNT_CONDITION_PRIFIX + MAXIMUM_AMOUNT;

            parameters.put("maxAmount", maxAmount);
        }

        TypedQuery<Expenditure> typedQuery = entityManager.createQuery(jpql, Expenditure.class)
                                                          .setParameter("member", member);

        // Map으로 저장한 parameter들을 query에 추가
        for(String key : parameters.keySet()) {
            typedQuery.setParameter(key, parameters.get(key));
        }

        // -- DB 조회

        List<Expenditure> expenditureList = typedQuery.getResultList();

        // 카테고리별 총합
        Map<String, Integer> categoryNameAndTotal =
                // 지출 합계 예외 항목 필터링
                expenditureList.stream().filter(expenditure -> !expenditure.getIsExcepted())
                        .collect(
                                // 카테고리명을 key로 해서 지출 금액을 각 카테고리에 맞게 value에 합함
                                // 세번째 parameter는 key값이 중복이 나올 경우 처리 방법을 입력
                                Collectors.toMap(expenditure -> expenditure.getBudgetByCategory().getCategory().getName(), Expenditure::getAmount, Integer::sum)
                        );

        // 총합
        int totalSum = expenditureList.stream().filter(expenditure -> !expenditure.getIsExcepted())
                                            .mapToInt(Expenditure::getAmount).sum();

        // Expenditure List를 ExpenditureDto List로 변환하여 ExpenditureSearchDto를 생성한 뒤 반환
        return ExpenditureSearchDto.Response.from(
                    expenditureList.stream().map(ExpenditureResponseDto::from).toList(),
                    categoryNameAndTotal,
                    totalSum
        );
    }

    /**
     * 하나의 지출에 대한 상세 데이터를 반환합니다.
     * */
    public ExpenditureResponseDto getExpenditureDetail(Long expenditureId) {

        // 주어진 id 값으로 지출 데이터를 DB에서 조회
        Expenditure expenditure = expenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found Expenditure"));

        return ExpenditureResponseDto.from(expenditure);
    }

    /**
     * 해당하는 지출 데이터를 삭제합니다.
     * */
    @Transactional
    public void deleteExpenditure(Long expenditureId) {
        expenditureRepository.deleteById(expenditureId);
    }


    private Member checkMember(User user) {
        return memberRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Not Found Username"));
    }
}
