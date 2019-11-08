package com.depromeet.booboo.domain.category;

import com.depromeet.booboo.domain.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Category {
    private static final Set<String> DEFAULT_CATEGORY_NAMES;

    static {
        DEFAULT_CATEGORY_NAMES = Stream.of(DefaultCategories.values())
                .map(DefaultCategories::value)
                .collect(Collectors.toSet());
    }

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
                                  String name,
                                  CategoryRepository categoryRepository) {
        Assert.notNull(memberId, "'memberId' must not be null");
        Assert.hasText(name, "'name' must not be null, empty or blank");
        Assert.notNull(categoryRepository, "'categoryRepository' must not be null");

        if (categoryRepository.existsByMemberIdAndName(memberId, name)) {
            throw new CategoryDuplicatedException("'name' already used. memberId: " + memberId + ", name: " + name);
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

        return categoryRepository.findByNameAndMemberId(name, memberId)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.memberId = memberId;
                    category.name = name;
                    return categoryRepository.save(category);
                });
    }

    public static List<Category> createDefaultCategories(Member member, CategoryRepository categoryRepository) {
        Assert.notNull(member, "'member' must not be null");
        Assert.notNull(categoryRepository, "'categoryRepository' must not be null");

        return DEFAULT_CATEGORY_NAMES.stream()
                .map(name -> Category.create(member.getMemberId(), name, categoryRepository))
                .collect(Collectors.toList());
    }

    public enum DefaultCategories {
        SUPPLIES("생활용품"),
        BABY_PRODUCTS("육아용품"),
        CULTURE("문화"),
        HEALTH("건강"),
        UNKNOWN("미등록");

        private final String value;

        DefaultCategories(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
}
