package com.depromeet.booboo.domain.member;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
@Builder
@ToString
@EqualsAndHashCode
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;
    private SnsType snsType;
    private String appId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiredAt;
    @Column(unique = true)
    private String connectionCode;
    private String name;
    private String profileImg;
    private Long spouseId;
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;
    private String providerUserId;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
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

    public enum SnsType {
        KAKAO
    }
}
