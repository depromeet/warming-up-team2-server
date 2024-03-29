package com.depromeet.booboo.domain.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByMemberIdAndName(Long memberId, String name);

    boolean existsByNameAndMemberIdIn(String name, List<Long> memberIds);

    boolean existsByCategoryIdAndMemberIdIn(Long categoryId, List<Long> memberIds);

    Optional<Category> findByNameAndMemberIdIn(String name, List<Long> memberIds);

    Optional<Category> findByNameAndMemberId(String name, Long memberId);

    Page<Category> findByMemberIdIn(List<Long> memberIds, Pageable pageable);

    List<Category> findByMemberIdIn(List<Long> memberIds);
}
