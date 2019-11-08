package com.depromeet.booboo.application.assembler;

import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.domain.expenditure.Expenditure;
import com.depromeet.booboo.domain.expenditure.ExpenditureValue;
import com.depromeet.booboo.ui.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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
        response.setCategory(Optional.ofNullable(expenditure.getCategory())
                .map(Category::getName)
                .orElse(Category.DefaultCategories.UNKNOWN.name()));
        response.setExpendedAt(expenditure.getExpendedAt());
        response.setCreatedAt(expenditure.getCreatedAt());
        response.setUpdatedAt(expenditure.getUpdatedAt());
        return response;
    }

    public MonthlyTotalExpenditureResponse toMonthlyTotalExpenditureResponse(List<Expenditure> expenditures) {
        if (expenditures == null) {
            return null;
        }
        Map<String, Long> map = expenditures.stream()
                .collect(Collectors.toMap(
                        it -> it.getExpendedAt().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Expenditure::getAmountOfMoney,
                        Long::sum,
                        TreeMap::new)
                );
        MonthlyTotalExpenditureResponse response = new MonthlyTotalExpenditureResponse();
        response.setMonthlyTotalExpenditures(map);
        return response;
    }

    public Map<String, MonthlyExpenditureResponse> toMonthlyExpenditureMap(List<Expenditure> expenditures) {
        if (expenditures == null) {
            return null;
        }

        Map<LocalDate, List<Expenditure>> monthlyExpenditureMap = new LinkedHashMap<>();
        expenditures.stream()
                .map(Expenditure::getExpendedAt)
                .sorted(Comparator.reverseOrder())
                .distinct()
                .forEach(expendedAt -> monthlyExpenditureMap.putIfAbsent(
                        expendedAt,
                        expenditures.stream()
                                .filter(expenditure -> {
                                    String source = expenditure.getExpendedAt().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                                    String target = expendedAt.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                                    return Objects.equals(source, target);
                                })
                                .collect(Collectors.toList())
                ));

        Map<String, MonthlyExpenditureResponse> dailyExpenditureMap = new LinkedHashMap<>();
        monthlyExpenditureMap.forEach((extendedAt, monthlyExpenditures) -> dailyExpenditureMap.put(
                extendedAt.format(DateTimeFormatter.ofPattern("yyyy-MM")),
                this.toMonthlyExpenditureResponse(extendedAt, monthlyExpenditures)
        ));
        return dailyExpenditureMap;
    }

    // 월별
    private MonthlyExpenditureResponse toMonthlyExpenditureResponse(LocalDate localDate, List<Expenditure> expenditures) {
        MonthlyExpenditureResponse response = new MonthlyExpenditureResponse();
        response.setTotal(expenditures.stream()
                .map(Expenditure::getAmountOfMoney)
                .reduce(0L, Long::sum));
        response.setFromAt(expenditures.stream()
                .map(Expenditure::getExpendedAt)
                .min(LocalDate::compareTo)
                .orElse(localDate.withDayOfMonth(1)));
        response.setToAt(expenditures.stream()
                .map(Expenditure::getExpendedAt)
                .max(LocalDate::compareTo)
                .orElse(localDate.plusMonths(1).withDayOfMonth(1).minusDays(1)));

        List<LocalDate> localDates = expenditures.stream()
                .map(Expenditure::getExpendedAt)
                .sorted(Comparator.reverseOrder())
                .distinct()
                .collect(Collectors.toList());

        Map<String, DailyExpenditureResponse> dailyExpenditureResponseMap = localDates.stream()
                .collect(Collectors.toMap(
                        expendedAt -> expendedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        expendedAt -> this.toDailyExpenditureResponse(
                                expendedAt,
                                expenditures.stream()
                                        .filter(expenditure -> {
                                            String source = expenditure.getExpendedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                            String target = expendedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                            return Objects.equals(source, target);
                                        })
                                        .collect(Collectors.toList()))
                ));
        response.setDailyExpenditures(dailyExpenditureResponseMap);
        return response;
    }

    // 일별
    private DailyExpenditureResponse toDailyExpenditureResponse(LocalDate localDate, List<Expenditure> expenditures) {
        if (expenditures == null) {
            return null;
        }
        DailyExpenditureResponse response = new DailyExpenditureResponse();
        response.setTotal(expenditures.stream().map(Expenditure::getAmountOfMoney).reduce(0L, Long::sum));
        response.setDayOfWeek(localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREA));
        response.setExpenditures(expenditures.stream()
                .sorted(Comparator.reverseOrder())
                .map(this::toExpenditureResponse)
                .collect(Collectors.toList()));
        return response;
    }


    public ExpenditureValue toExpenditureValue(ExpenditureRequest expenditureRequest) {
        Assert.notNull(expenditureRequest, "'expenditureRequest' must not be null");

        ExpenditureValue value = new ExpenditureValue();
        value.setAmountOfMoney(expenditureRequest.getAmountOfMoney());
        value.setTitle(expenditureRequest.getTitle());
        value.setDescription(expenditureRequest.getDescription());
        value.setCategory(expenditureRequest.getCategory());
        value.setPaymentMethod(expenditureRequest.getPaymentMethod());
        value.setExpendedAt(expenditureRequest.getExpendedAt());
        return value;
    }
}
