package com.depromeet.booboo.domain.expenditure;

import com.depromeet.booboo.domain.member.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Long amountOfMoney;
    private String title;
    private String description;
    private String imageUrl;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Expenditure create(Member member, Long amountOfMoney, String title, String description) {
        Expenditure expenditure = new Expenditure();
        expenditure.member = member;
        expenditure.amountOfMoney = amountOfMoney;
        expenditure.title = title;
        expenditure.description = description;

        expenditure.validate();
        return expenditure;
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
        Assert.hasText(this.description, "'description' must not be null, empty or blank");
    }
}
