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
import java.time.LocalDate;
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
    @ManyToOne
    private Category category;
    private Long amountOfMoney;
    private String title;
    private String description;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;
    private LocalDate expendedAt;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Expenditure create(Member member, ExpenditureValue expenditureValue, Category category) {
        Assert.notNull(member, "'member' must not be null");
        Assert.notNull(expenditureValue, "'expenditureValue' must not be null");

        Expenditure expenditure = new Expenditure();
        expenditure.member = member;
        expenditure.amountOfMoney = expenditureValue.getAmountOfMoney();
        expenditure.title = expenditureValue.getTitle();
        String description = expenditureValue.getDescription();
        if (!StringUtils.isEmpty(description)) {
            expenditure.description = description;
        }
        expenditure.paymentMethodType = PaymentMethodType.from(expenditureValue.getPaymentMethod());
        LocalDate expendedAt = expenditureValue.getExpendedAt();
        if (expendedAt != null) {
            expenditure.expendedAt = expendedAt;
        }
        if (category != null) {
            expenditure.category = category;
        }
        expenditure.validate();
        return expenditure;
    }

    public Expenditure update(Long memberId, List<Long> memberIds, ExpenditureValue expenditureValue, CategoryRepository categoryRepository) {
        Assert.notNull(expenditureValue, "'expenditureValue' must not be null");

        Long amountOfMoney = expenditureValue.getAmountOfMoney();
        if (amountOfMoney != null) {
            this.amountOfMoney = amountOfMoney;
        }
        String title = expenditureValue.getTitle();
        if (title != null) {
            this.title = title;
        }
        String description = expenditureValue.getDescription();
        if (description != null) {
            this.description = description;
        }
        String categoryName = expenditureValue.getCategory();
        if (!StringUtils.isEmpty(categoryName)) {
            this.category = Category.getOrCreate(memberId, memberIds, categoryName, categoryRepository);
        }
        this.paymentMethodType = PaymentMethodType.from(expenditureValue.getPaymentMethod());
        LocalDate expendedAt = expenditureValue.getExpendedAt();
        if (expendedAt != null) {
            this.expendedAt = expendedAt;
        }
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
        Assert.notNull(paymentMethodType, "'paymentMethodType' must not be null");
        Assert.notNull(this.expendedAt, "'this.expendedAt' must not be null");
    }
}
