package com.example.money.domain.budget.service;

import com.example.money.domain.budget.dto.BudgetSetDto;
import com.example.money.domain.budget.dto.BudgetSuggestResponseDto;
import com.example.money.domain.budget.entity.Budget;
import com.example.money.domain.budget.repository.BudgetRepository;
import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import com.example.money.domain.budgetByCategory.repository.BudgetByCategoryRepository;
import com.example.money.domain.category.entity.Category;
import com.example.money.domain.category.repository.CategoryRepository;
import com.example.money.domain.member.entity.Member;
import com.example.money.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetByCategoryRepository budgetByCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    /**
     * 사용자가 지정한 예산 내역을 등록합니다.
     * */
    @Transactional
    public BudgetSetDto.Response setBudget(User user, BudgetSetDto.Request request) {

        Member member = checkMember(user);

        if(isDuplicateBudget(member, request))
            throw new IllegalArgumentException("already created budget");

        // 예산 데이터를 먼저 DB에 저장합니다.
        Budget budget = budgetRepository.save(request.toBudget(member));

        // 사용자의 category List을 DB에서 가져옵니다.
        List<Category> categories = categoryRepository.findByMemberId(member.getId());
        // request의 category id와 category amount를 가져옵니다.
        Map<Long, Integer> categoryIdAndAmount = request.getCategoryIdAndAmount();

        // request에서 amount가 있는 category와 일치하는 사용자의 category를 사용자의 List에서 필터링한 후
        List<BudgetByCategory> budgetByCategories = categories.stream().filter(category -> categoryIdAndAmount.containsKey(category.getId()))
                // DB에 저장하기 위한 카테고리별 예산 List를 생성합니다.
                .map(category -> BudgetByCategory.builder()
                        .budget(budget)
                        .category(category)
                        .amount(categoryIdAndAmount.get(category.getId()))
                        .build()
                ).toList();

        // 카테고리별 예산을 DB에 저장합니다.
        budgetByCategoryRepository.saveAll(budgetByCategories);

        // Response를 생성해 반환합니다.
        return BudgetSetDto.Response.from(budget, budgetByCategories);
    }

    public List<BudgetSuggestResponseDto> suggestBudget(int total) {

        // 예산 추천을 위한 데이터 조회 시작일자를 지난달 1일로 합니다.
        LocalDate lastMonth = LocalDate.now().minusMonths(1L).withDayOfMonth(1);
//        LocalDate lastMonth = LocalDate.of(2023, 10, 1);

        // 조회 시작일 이후에 해당하는 전체 사용자의 카테고리별 예산과 카테고리, 예산을 모두 조회 합니다.
        List<BudgetByCategory> budgetByCategories = budgetByCategoryRepository.findAllBudgetByCategoryInOneMonthWithCategoryAndBudget(lastMonth);

        // 카테고리 이름을 key로 하여 카테고리 별 예산 설정금액의 비율을 모두 더한 Map을 생성합니다.
        Map<String, Float> categoryNameAndRatio = budgetByCategories.stream().collect(
                Collectors.toMap(
                        budgetByCategory -> budgetByCategory.getCategory().getName(),
                        // 각각의 예산을 총 예산으로 나눕니다.
                        budgetByCategory -> (float) budgetByCategory.getAmount() / (float) budgetByCategory.getBudget().getTotal(),
                        Float::sum
                )
        );

        // 카테고리 이름을 key로 하여 카테고리 별 예산 데이터의 총 개수를 가진 Map을 생성합니다.
        Map<String, Integer> categoryNameAndCountOfCategory = budgetByCategories.stream().collect(
                Collectors.toMap(
                        budgetByCategory -> budgetByCategory.getCategory().getName(),
                        budgetByCategory -> 1,
                        Integer::sum)
        );

        // 위의 두 map을 이용하여 categoryNameAndAverage의 value를 평균 비율로 변경합니다.
        categoryNameAndRatio.replaceAll((name, avg) -> categoryNameAndRatio.get(name) / categoryNameAndCountOfCategory.get(name));

        // total 값에 맞는 각 카테고리별 예산 금액을 구하고, 카테고리별 추천 예산을 List로 만들어 반환합니다.
        return BudgetSuggestResponseDto.ListOf(categoryNameAndRatio, total);
    }

    /**
     * 사용자의 아이디로 사용자가 존재하는지 확인합니다.
     * @param user org.springframework.security.core.userdetails.User
     * */
    private Member checkMember(User user) {
        return memberRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Not Found Username"));
    }

    /**
     * 사용자 예산 설정이 지정된 일자 기준으로 이미 존재하는지 확인합니다.
     * */
    private boolean isDuplicateBudget(Member member,BudgetSetDto.Request request) {
        return budgetRepository.existsByStartDateAndEndDateAndMemberId(request.getStartDate(), request.getEndDate(), member.getId());
    }

}