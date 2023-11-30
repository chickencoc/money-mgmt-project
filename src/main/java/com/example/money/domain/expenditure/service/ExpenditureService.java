package com.example.money.domain.expenditure.service;

import com.example.money.domain.expenditure.dto.ExpenditureDto;
import com.example.money.domain.expenditure.dto.ExpenditureUpdateDto;
import com.example.money.domain.expenditure.entity.Expenditure;
import com.example.money.domain.expenditure.repository.ExpenditureRepository;
import com.example.money.domain.member.entity.Member;
import com.example.money.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenditureService {

    private MemberRepository memberRepository;
    private ExpenditureRepository expenditureRepository;

    /**
     * 사용자의 새로운 지출 목록을 생성합니다.
     * */
    @Transactional
    public List<ExpenditureDto.Response> saveExpenditures(User user, List<ExpenditureDto.Request> requestList) {

        Member member = checkMember(user);

        // budget, category 데이터가 존재하는지 확인합니다.


        // request를 이용해 expenditure 객체를 생성합니다.
        List<Expenditure> expenditureList = requestList.stream().map(request -> request.toExpenditure(member)).toList();

        // expenditure 객체를 DB에 저장하고
        return expenditureRepository.saveAll(expenditureList)
                // expenditure를 dto로 변환하여 반환합니다.
                .stream().map(ExpenditureDto.Response::from).toList();
    }

    /**
     * 사용자의 지출 목록을 수정합니다.
     * */
    @Transactional
    public ExpenditureDto.Response updateExpenditure(User user, ExpenditureUpdateDto request) {

        Member member = checkMember(user);

        // request에 주어진 id 값으로 지출 데이터를 DB에서 가져옵니다.
        Expenditure expenditure = expenditureRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Not Found Expenditure"));

        // 지출 데이터를 수정합니다. todo: dirty check?
        expenditure.update(request);

        return ExpenditureDto.Response.from(expenditure);
    }

    /**
     * 사용자의 지출 목록을 조회합니다. 기본으로 월단위 목록이 조회 되며, 일자 또는 금액의 기준을 추가하여 목록을 조회할 수 있습니다.
     * 목록을 반환할 때 전체 합계와 카테고리별 합계를 제공합니다.
     * */
    public List<ExpenditureDto> getExpendituresByCondition(User user, String period, LocalDate start, LocalDate end, Integer min, Integer max) {

        Member member = checkMember(user);

        // todo: 지출 목록 조회

        return null;
    }

    /**
     * 하나의 지출에 대한 상세 데이터를 반환합니다.
     * */
    public ExpenditureDto.Response getExpenditureDetail(Long expenditureId) {

        // 주어진 id 값으로 지출 데이터를 DB에서 가져옵니다.
        Expenditure expenditure = expenditureRepository.findById(expenditureId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found Expenditure"));

        return ExpenditureDto.Response.from(expenditure);
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
