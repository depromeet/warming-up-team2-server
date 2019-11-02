package com.depromeet.booboo.domain.member;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * - 사용자 ID (memberId Long)
 * - SNS 타입 (snsType Enum)
 * - 앱 id (appId String)
 * - Access Token (accessToken String)
 * - Refresh Token (refreshToken String)
 * - 토큰 만료일 (tokenExpireDate LocalDateTime)
 * - 이름 (name String)
 * - 프로필 이미지 (profileImg String)
 * - 배우자 ID (spouseId Long)
 * - 등록일 (createDate LocalDateTime)
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id @GeneratedValue
    private Long memberId;
    private SnsType snsType;
    private String appId;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpireDate;
    private String name;
    private String profileImg;
    private Long spouseId;
    private LocalDateTime createDate;

    public enum SnsType {
        KAKAO
    }
}
