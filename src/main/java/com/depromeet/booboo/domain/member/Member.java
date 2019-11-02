package com.depromeet.booboo.domain.member;

import com.depromeet.booboo.domain.couple.Couple;
import com.depromeet.booboo.domain.couple.CoupleConnectionFailedException;
import com.depromeet.booboo.domain.couple.CoupleRepository;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * - 사용자 ID (memberId Long)
 * - SNS 타입 (snsType Enum)
 * - 앱 id (appId String)
 * - Access Token (accessToken String)
 * - Refresh Token (refreshToken String)
 * - 토큰 만료일 (tokenExpiredAt LocalDateTime)
 * - 이름 (name String)
 * - 프로필 이미지 (profileImg String)
 * - 연결 코드 (connectionCode String)
 * - 배우자 ID (spouseId Long)
 * - 등록일 (createdAt LocalDateTime)
 * - 갱신일 (updatedAt LocalDateTime)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;
    @Column(unique = true)
    private String connectionCode;
    private String name;
    private String profileImg;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    private String providerUserId;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    @ManyToOne
    private Couple couple;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Member fromKakao(String name, String profileImageUrl, String providerUserId, String connectionCode) {
        Member member = new Member();
        member.name = name;
        member.profileImg = profileImageUrl;
        member.providerType = ProviderType.KAKAO;
        member.providerUserId = providerUserId;
        member.status = MemberStatus.initial();
        member.connectionCode = connectionCode;
        return member;
    }

    public Couple connect(Member spouse, CoupleRepository coupleRepository) {
        Assert.notNull(spouse, "'spouse' must not be null");

        if (this.memberId.equals(spouse.getMemberId())) {
            throw new CoupleConnectionFailedException("Cannot connect with oneself");
        }

        this.status = this.status.connect();
        spouse.status = spouse.status.connect();

        Couple couple = new Couple();
        this.couple = couple;
        spouse.couple = couple;

        return coupleRepository.save(couple);
    }
}
