package com.depromeet.booboo.application.service;

import com.depromeet.booboo.ui.dto.CategoryRequest;
import com.depromeet.booboo.ui.dto.CategoryResponse;
import com.depromeet.booboo.ui.dto.MostExpendedCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponse createCategory(Long memberId, CategoryRequest categoryRequest);

    Page<CategoryResponse> getCategories(Long memberId, Pageable pageable);

    MostExpendedCategoryResponse getCategoriesOrderByTotalExpenditure(Long memberId);
}
