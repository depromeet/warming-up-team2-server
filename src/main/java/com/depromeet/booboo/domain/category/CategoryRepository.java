package com.depromeet.booboo.domain.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndMemberIdIn(String name, List<Long> memberIds);

    Page<Category> findByMemberIdIn(List<Long> memberIds, Pageable pageable);
}
