package com.depromeet.booboo.application.service.impl;

import com.depromeet.booboo.application.assembler.ExpenditureAssembler;
import com.depromeet.booboo.application.service.ExpenditureService;
import com.depromeet.booboo.domain.expenditure.Expenditure;
import com.depromeet.booboo.domain.expenditure.ExpenditureException;
import com.depromeet.booboo.domain.expenditure.ExpenditureRepository;
import com.depromeet.booboo.domain.member.Member;
import com.depromeet.booboo.domain.member.MemberRepository;
import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class ExpenditureServiceImpl implements ExpenditureService {
    private final MemberRepository memberRepository;
    private final ExpenditureRepository expenditureRepository;
    private final ExpenditureAssembler expenditureAssembler;

    @Override
    @Transactional
    public ExpenditureResponse createExpenditure(Long memberId, ExpenditureRequest expenditureRequest) {
        Assert.notNull(expenditureRequest, "'expenditureRequest' must not be null");

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExpenditureException("member not found. memberId:" + memberId));

        Expenditure expenditure = Expenditure.create(
                member,
                expenditureRequest.getAmountOfMoney(),
                expenditureRequest.getTitle(),
                expenditureRequest.getDescription()
        );
        expenditureRepository.save(expenditure);
        return expenditureAssembler.toExpenditureResponse(expenditure);
    }
}
