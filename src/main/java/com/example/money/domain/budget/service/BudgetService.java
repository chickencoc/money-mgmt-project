package com.example.money.domain.budget.service;

import com.example.money.domain.budget.BudgetSetDto;
import com.example.money.domain.budget.entity.Budget;
import com.example.money.domain.budget.repository.BudgetRepository;
import com.example.money.domain.budgetByCategory.dto.BudgetByCategoryResponseDto;
import com.example.money.domain.budgetByCategory.entity.BudgetByCategory;
import com.example.money.domain.budgetByCategory.repository.BudgetByCategoryRepository;
import com.example.money.domain.category.entity.Category;
import com.example.money.domain.category.repository.CategoryRepository;
import com.example.money.domain.member.entity.Member;
import com.example.money.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public BudgetSetDto.Response setBudget(User user, BudgetSetDto.Request request) {

        Member member = checkMember(user);

        if(isDuplicateBudget(member, request))
            throw new IllegalArgumentException("already created budget");

        // 예산 데이터를 먼저 DB에 저장합니다.
        Budget budget = budgetRepository.save(request.toBudget(member));

        // 사용자의 카테고리 목록을 DB에서 가져옵니다.
        List<Category> categories = categoryRepository.findByMemberId(member.getId());
        Map<Long, Integer> categoryIdAndAmount = request.getCategoryIdAndAmount();

        // 사용자의 카테고리 목록에서 예산 값이 있는 카테고리만 남긴 후
        List<BudgetByCategory> budgetByCategories = categories.stream().filter(category -> categoryIdAndAmount.containsKey(category.getId()))
                // 카테고리별 예산 List를 생성합니다.
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

    public List<BudgetByCategoryResponseDto> suggestBudget(int total) {

        LocalDate today = LocalDate.now().minusMonths(1L);

        List<BudgetByCategory> budgetByCategories = budgetByCategoryRepository.findAllBudgetByCategoryInOneMonthWithCategoryAndBudget(today);

        // todo: 평균값 만들어야함.
//        budgetByCategories.stream().map(budgetByCategory -> b)
        return null;
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