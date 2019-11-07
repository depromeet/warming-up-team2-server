package com.depromeet.booboo.application.service.impl;

import com.depromeet.booboo.application.assembler.CategoryAssembler;
import com.depromeet.booboo.application.exception.ResourceNotFoundException;
import com.depromeet.booboo.application.service.CategoryService;
import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.domain.category.CategoryRepository;
import com.depromeet.booboo.domain.expenditure.Expenditure;
import com.depromeet.booboo.domain.expenditure.ExpenditureRepository;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.domain.member.MemberRepository;
import com.depromeet.booboo.ui.dto.CategoryRequest;
import com.depromeet.booboo.ui.dto.CategoryResponse;
import com.depromeet.booboo.ui.dto.MostExpendedCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryAssembler categoryAssembler;
    private final ExpenditureRepository expenditureRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(Long memberId, CategoryRequest categoryRequest) {
        Assert.notNull(categoryRequest, "'categoryRequest' must not be null");

        String name = categoryRequest.getName();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("'member' not found. memberId:" + memberId));

        Category category = Category.create(member.getMemberId(), name, categoryRepository);

        return categoryAssembler.toCategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(Long memberId, Pageable pageable) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.notNull(pageable, "'pageable' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("'member' not found. memberId:" + memberId));
        List<Long> memberIds = member.getCoupleMembers()
                .stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        Page<Category> categoryPage = categoryRepository.findByMemberIdIn(memberIds, pageable);
        return new PageImpl<>(
                categoryPage.stream()
                        .map(categoryAssembler::toCategoryResponse)
                        .collect(Collectors.toList()),
                pageable,
                categoryPage.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MostExpendedCategoryResponse getCategoriesOrderByTotalExpenditure(Long memberId) {
        Assert.notNull(memberId, "'memberId' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("'member' not found. memberId:" + memberId));
        List<Long> memberIds = member.getCoupleMembers()
                .stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());

        List<Category> categories = categoryRepository.findByMemberIdIn(memberIds);
        List<Expenditure> expenditures = expenditureRepository.findByMemberIn(member.getCoupleMembers());

        return categoryAssembler.toCategoryByExpenditureResponse(categories, expenditures);
    }
}
