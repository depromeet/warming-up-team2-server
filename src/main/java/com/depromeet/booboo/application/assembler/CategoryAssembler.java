package com.depromeet.booboo.application.assembler;

import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.domain.expenditure.Expenditure;
import com.depromeet.booboo.domain.utils.MapUtils;
import com.depromeet.booboo.ui.dto.CategoryResponse;
import com.depromeet.booboo.ui.dto.MostExpendedCategoryResponse;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CategoryAssembler {
    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getCategoryId());
        response.setName(category.getName());
        return response;
    }

    public MostExpendedCategoryResponse toCategoryByExpenditureResponse(List<Category> categories,
                                                                        List<Expenditure> expenditures) {
        if (categories == null) {
            return null;
        }
        if (expenditures == null) {
            Map<String, Long> map = new LinkedHashMap<>();
            categories.forEach(it -> map.put(it.getName(), 0L));
            MostExpendedCategoryResponse response = new MostExpendedCategoryResponse();
            response.setCategoryMap(map);
            return response;
        }
        Map<String, Long> map = categories.stream()
                .collect(Collectors.toMap(
                        Category::getName,
                        it -> expenditures.stream()
                                .filter(expenditure -> it.equals(expenditure.getCategory()))
                                .map(Expenditure::getAmountOfMoney)
                                .reduce(0L, Long::sum)
                ));

        Map<String, Long> sortedMap = MapUtils.sortByValue(map, Map.Entry.comparingByValue(Comparator.reverseOrder()));

        MostExpendedCategoryResponse response = new MostExpendedCategoryResponse();
        response.setCategoryMap(sortedMap);
        return response;
    }
}
