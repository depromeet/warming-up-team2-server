package com.depromeet.booboo.domain.category;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    private Long memberId;
    private String name;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Category create(Long memberId,
                                  List<Long> memberIds,
                                  String name,
                                  CategoryRepository categoryRepository) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.hasText(name, "'name' must not be null, empty or blank");
        Assert.notNull(categoryRepository, "'categoryRepository' must not be null");

        if (!categoryRepository.existsByNameAndMemberIdIn(name, memberIds)) {
            throw new CategoryDuplicatedException("'name' already used. name:" + name);
        }

        Category category = new Category();
        category.memberId = memberId;
        category.name = name;
        return categoryRepository.save(category);
    }

    public static Category getOrCreate(Long memberId,
                                       List<Long> memberIds,
                                       String name,
                                       CategoryRepository categoryRepository) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.hasText(name, "'name' must not be null, empty or blank");
        Assert.notNull(categoryRepository, "'categoryRepository' must not be null");

        return categoryRepository.findByNameAndMemberIdIn(name, memberIds)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.memberId = memberId;
                    category.name = name;
                    return categoryRepository.save(category);
                });
    }
}
