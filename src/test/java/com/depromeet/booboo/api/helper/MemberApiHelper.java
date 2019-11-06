package com.depromeet.booboo.api.helper;

import com.depromeet.booboo.api.LoginControllerApi;
import com.depromeet.booboo.api.TestApiResult;
import com.depromeet.booboo.application.adapter.kakao.KakaoAdapter;
import com.depromeet.booboo.application.adapter.kakao.KakaoUserResponse;
import com.depromeet.booboo.ui.dto.LoginRequest;
import com.depromeet.booboo.ui.dto.LoginResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MemberApiHelper {
    private final KakaoAdapter kakaoAdapter;
    private final LoginControllerApi loginControllerApi;

    public MemberApiHelper(KakaoAdapter kakaoAdapter, LoginControllerApi loginControllerApi) {
        this.kakaoAdapter = kakaoAdapter;
        this.loginControllerApi = loginControllerApi;
    }

    public String createOAuthUser(String kakaoAccessToken, Long providerUserId, String name, String profileImageUrl) throws Exception {
        KakaoUserResponse kakaoUserResponse = mock(KakaoUserResponse.class);
        when(kakaoAdapter.getUserInfo(kakaoAccessToken)).thenReturn(kakaoUserResponse);
        when(kakaoUserResponse.getId()).thenReturn(providerUserId);
        when(kakaoUserResponse.getUserName()).thenReturn(name);
        when(kakaoUserResponse.getProfileImage()).thenReturn(profileImageUrl);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccessToken(kakaoAccessToken);

        TestApiResult<LoginResponse> loginResult = loginControllerApi.login(loginRequest);
        return loginResult.getApiResponse().getData().getAccessToken();
    }


}
