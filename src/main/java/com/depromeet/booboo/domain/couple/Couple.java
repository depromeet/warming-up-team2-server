package com.depromeet.booboo.domain.couple;

import com.depromeet.booboo.domain.member.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Couple {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long coupleId;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "couple")
    private List<Member> members = new LinkedList<>();

    public String getSpouseName(Member member) {
        Assert.notNull(member, "'member' must not be null");
        return members.stream()
                .filter(it -> !it.equals(member))
                .map(Member::getName)
                .findFirst()
                .orElse(null);
    }
}
