package com.depromeet.booboo.application.assembler;

import com.depromeet.booboo.domain.expenditure.Expenditure;
import com.depromeet.booboo.domain.expenditure.ExpenditureUpdateValue;
import com.depromeet.booboo.ui.dto.ExpenditureRequest;
import com.depromeet.booboo.ui.dto.ExpenditureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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
        response.setPaymentMethod(expenditure.getPaymentMethodType().name());
        response.setCreatedAt(expenditure.getCreatedAt());
        response.setUpdatedAt(expenditure.getUpdatedAt());
        return response;
    }

    public ExpenditureUpdateValue toExpenditureUpdateValue(ExpenditureRequest expenditureRequest) {
        Assert.notNull(expenditureRequest, "'expenditureRequest' must not be null");

        ExpenditureUpdateValue value = new ExpenditureUpdateValue();
        value.setAmountOfMoney(expenditureRequest.getAmountOfMoney());
        value.setTitle(expenditureRequest.getTitle());
        value.setDescription(expenditureRequest.getDescription());
        value.setCategory(expenditureRequest.getCategory());
        value.setPaymentMethod(expenditureRequest.getPaymentMethod());
        return value;
    }
}
