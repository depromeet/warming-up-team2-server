package com.depromeet.booboo.application.service.impl;

import com.depromeet.booboo.application.adapter.storage.StorageAdapter;
import com.depromeet.booboo.application.assembler.ExpenditureAssembler;
import com.depromeet.booboo.application.service.ExpenditureService;
import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.domain.category.CategoryRepository;
import com.depromeet.booboo.domain.expenditure.Expenditure;
import com.depromeet.booboo.domain.expenditure.ExpenditureException;
import com.depromeet.booboo.domain.expenditure.ExpenditureRepository;
import com.depromeet.booboo.domain.expenditure.ExpenditureValue;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.domain.member.MemberRepository;
import com.depromeet.booboo.ui.dto.ExpenditureQueryRequest;
import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import com.depromeet.booboo.ui.dto.MonthlyTotalExpenditureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenditureServiceImpl implements ExpenditureService {
    private final MemberRepository memberRepository;
    private final ExpenditureRepository expenditureRepository;
    private final ExpenditureAssembler expenditureAssembler;
    private final CategoryRepository categoryRepository;
    private final StorageAdapter storageAdapter;

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenditureResponse> getExpenditures(Long memberId, ExpenditureQueryRequest expenditureQueryRequest, Pageable pageable) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.notNull(expenditureQueryRequest, "'expenditureQueryRequest' must not be null");
        Assert.notNull(pageable, "'pageable' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExpenditureException("member not found. memberId:" + memberId));

        List<Member> members = member.getCoupleMembers();


        String categoryName = expenditureQueryRequest.getCategory();
        Page<Expenditure> expenditurePage;
        // TODO: QueryDSL
        if (categoryName == null) {
            expenditurePage = expenditureRepository.findByMemberIn(members, pageable);
        } else {
            Category category = categoryRepository.findByNameAndMemberIdIn(categoryName, members.stream()
                    .map(Member::getMemberId)
                    .collect(Collectors.toList()))
                    .orElse(null);
            if (category == null) {
                expenditurePage = expenditureRepository.findByMemberIn(members, pageable);
            } else {
                expenditurePage = expenditureRepository.findByMemberInAndCategory(members, category, pageable);
            }
        }

        return new PageImpl<>(
                expenditurePage.map(expenditureAssembler::toExpenditureResponse).toList(),
                pageable,
                expenditurePage.getTotalElements()
        );
    }

    @Override
    @Transactional
    public ExpenditureResponse createExpenditure(Long memberId, ExpenditureRequest expenditureRequest) {
        Assert.notNull(expenditureRequest, "'expenditureRequest' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExpenditureException("member not found. memberId:" + memberId));

        List<Long> memberIds = member.getCoupleMembers()
                .stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());
        String categoryName = Optional.ofNullable(expenditureRequest.getCategory())
                .orElse(Category.DefaultCategories.UNKNOWN.value());

        Expenditure expenditure = Expenditure.create(
                member,
                expenditureAssembler.toExpenditureValue(expenditureRequest),
                Category.getOrCreate(memberId, memberIds, categoryName, categoryRepository)
        );
        expenditureRepository.save(expenditure);
        return expenditureAssembler.toExpenditureResponse(expenditure);
    }

    @Override
    @Transactional
    public ExpenditureResponse updateExpenditure(Long memberId, Long expenditureId, ExpenditureRequest expenditureRequest) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.notNull(expenditureId, "'expenditureId' must not be null");
        Assert.notNull(expenditureRequest, "'expenditureRequest' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExpenditureException("member not found. memberId:" + memberId));
        Expenditure expenditure = expenditureRepository.findByMemberAndExpenditureId(member, expenditureId)
                .orElseThrow(() -> new ExpenditureException("expenditure not found. memberId:" + memberId + ", expenditureId:" + expenditureId));

        List<Long> memberIds = member.getCoupleMembers()
                .stream()
                .map(Member::getMemberId)
                .collect(Collectors.toList());
        ExpenditureValue expenditureValue = expenditureAssembler.toExpenditureValue(expenditureRequest);
        expenditure.update(memberId, memberIds, expenditureValue, categoryRepository);
        return expenditureAssembler.toExpenditureResponse(expenditure);
    }

    @Override
    @Transactional
    public ExpenditureResponse updateImage(Long memberId, Long expenditureId, MediaType mediaType, InputStream inputStream) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.notNull(expenditureId, "'expenditureId' must not be null");
        Assert.notNull(inputStream, "'inputStream' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExpenditureException("member not found. memberId:" + memberId));
        Expenditure expenditure = expenditureRepository.findByMemberAndExpenditureId(member, expenditureId)
                .orElseThrow(() -> new ExpenditureException("expenditure not found. memberId:" + memberId + ", expenditureId:" + expenditureId));

        String imageUrl = storageAdapter.save(mediaType, inputStream);
        expenditure.updateImageUrl(imageUrl);
        return expenditureAssembler.toExpenditureResponse(expenditure);
    }

    @Override
    @Transactional(readOnly = true)
    public MonthlyTotalExpenditureResponse getTotalExpendituresMonthly(Long memberId) {
        Assert.notNull(memberId, "'memberId' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExpenditureException("member not found. memberId:" + memberId));
        LocalDate fromExpendedAt = LocalDate.now().withDayOfMonth(1).minusMonths(5L);
        List<Expenditure> expenditures = expenditureRepository.findByMemberInAndExpendedAtGreaterThanEqual(member.getCoupleMembers(), fromExpendedAt);
        return expenditureAssembler.toMonthlyTotalExpenditureResponse(expenditures);
    }
}
