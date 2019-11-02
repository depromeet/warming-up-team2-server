package com.depromeet.booboo.application.adapter.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class KakaoUserResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("properties")
    private Map<String, String> properties;

    @JsonProperty("kakao_account")
    private Map<String, Object> account;

    public String getUserName() {
        return this.properties.get("nickname");
    }

    public String getProfileImage() {
        return this.properties.get("profile_image");
    }
}

