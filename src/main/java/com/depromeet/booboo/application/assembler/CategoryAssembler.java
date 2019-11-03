package com.depromeet.booboo.application.assembler;

import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.ui.dto.CategoryResponse;
import org.springframework.stereotype.Component;

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
}
