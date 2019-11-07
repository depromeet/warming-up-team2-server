package com.depromeet.booboo.domain.expenditure;

import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.domain.category.CategoryRepository;
import com.depromeet.booboo.domain.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Expenditure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long expenditureId;
    @ManyToOne
    private Member member;
    private Long categoryId;
    private Long amountOfMoney;
    private String title;
    private String description;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Expenditure create(Member member,
                                     Long amountOfMoney,
                                     String title,
                                     String description,
                                     Long categoryId,
                                     String paymentMethod) {
        Expenditure expenditure = new Expenditure();
        expenditure.member = member;
        expenditure.amountOfMoney = amountOfMoney;
        expenditure.title = title;
        if (!StringUtils.isEmpty(description)) {
            expenditure.description = description;
        }
        if (categoryId != null) {
            expenditure.categoryId = categoryId;
        }
        expenditure.paymentMethodType = PaymentMethodType.from(paymentMethod);
        expenditure.validate();
        return expenditure;
    }

    public Expenditure update(Long memberId, List<Long> memberIds, ExpenditureUpdateValue expenditureUpdateValue, CategoryRepository categoryRepository) {
        Assert.notNull(expenditureUpdateValue, "'expenditureUpdateValue' must not be null");

        Long amountOfMoney = expenditureUpdateValue.getAmountOfMoney();
        if (amountOfMoney != null) {
            this.amountOfMoney = amountOfMoney;
        }
        String title = expenditureUpdateValue.getTitle();
        if (title != null) {
            this.title = title;
        }
        String description = expenditureUpdateValue.getDescription();
        if (description != null) {
            this.description = description;
        }
        String categoryName = expenditureUpdateValue.getCategory();
        if (!StringUtils.isEmpty(categoryName)) {
            this.categoryId = Category.getOrCreate(memberId, memberIds, categoryName, categoryRepository).getCategoryId();
        }
        this.paymentMethodType = PaymentMethodType.from(expenditureUpdateValue.getPaymentMethod());
        this.validate();
        return this;
    }

    public Expenditure updateImageUrl(String imageUrl) {
        Assert.hasText(imageUrl, "'imageUrl' must not be null, empty or blank");

        this.imageUrl = imageUrl;

        this.validate();
        return this;
    }

    private void validate() {
        Assert.notNull(this.member, "'member' must not be null");
        Assert.notNull(this.amountOfMoney, "'amountOfMoney' must not be null");
        if (this.amountOfMoney < 0) {
            throw new IllegalArgumentException("'amountOfMoney' must be greater than or equal to zero. amountOfMoney:" + amountOfMoney);
        }
        Assert.hasText(this.title, "'title' must not be null, empty or blank");
    }
}
