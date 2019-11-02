package com.depromeet.booboo.application.assembler;

import com.depromeet.booboo.domain.expenditure.Expenditure;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpenditureAssembler {
    private final MemberAssembler memberAssembler;

    public ExpenditureResponse toExpenditureResponse(Expenditure expenditure) {
        if (expenditure == null) {
            return null;
        }
        ExpenditureResponse response = new ExpenditureResponse();
        response.setId(expenditure.getExpenditureId());
        response.setMember(memberAssembler.toMemberResponse(expenditure.getMember()));
        response.setAmountOfMoney(expenditure.getAmountOfMoney());
        response.setTitle(expenditure.getTitle());
        response.setDescription(expenditure.getDescription());
        response.setImageUrl(expenditure.getImageUrl());
        response.setCreatedAt(expenditure.getCreatedAt());
        response.setUpdatedAt(expenditure.getUpdatedAt());
        return response;
    }
}
