package com.depromeet.booboo.domain.couple;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
