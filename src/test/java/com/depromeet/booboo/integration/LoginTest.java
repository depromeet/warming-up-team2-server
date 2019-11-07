package com.depromeet.booboo.integration;

import com.depromeet.booboo.api.LoginControllerApi;
import com.depromeet.booboo.api.MemberControllerApi;
import com.depromeet.booboo.api.TestApiResult;
import com.depromeet.booboo.application.adapter.kakao.KakaoAdapter;
import com.depromeet.booboo.application.adapter.kakao.KakaoUserResponse;
import com.depromeet.booboo.ui.dto.LoginRequest;
import com.depromeet.booboo.ui.dto.LoginResponse;
import com.depromeet.booboo.ui.dto.MemberResponse;
import com.depromeet.booboo.ui.dto.common.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTest {
    private static final TypeReference<ApiResponse<LoginResponse>> API_LOGIN_RESPONSE_TYPE_REFERENCE = new TypeReference<ApiResponse<LoginResponse>>() {
    };

    @MockBean
    private KakaoAdapter kakaoAdapter;
    @Mock
    private KakaoUserResponse kakaoUserResponse;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private LoginControllerApi loginControllerApi;
    private MemberControllerApi memberControllerApi;

    @Before
    public void setUp() throws Exception {
        when(kakaoAdapter.getUserInfo(anyString())).thenReturn(kakaoUserResponse);
        when(kakaoUserResponse.getId()).thenReturn(1L);
        when(kakaoUserResponse.getProfileImage()).thenReturn("kakaoProfileImageUrl");
        when(kakaoUserResponse.getUserName()).thenReturn("kakaoUsername");

        loginControllerApi = new LoginControllerApi(mockMvc, objectMapper);
        memberControllerApi = new MemberControllerApi(mockMvc, objectMapper);
    }

    @Test
    public void 카카오_계정으로_처음_로그인하는_경우__계정이_잘_생성되어야함() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccessToken("kakaoAccessToken");
        // when
        MvcResult mvcResult = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest)))
                // then 1
                .andExpect(status().isOk())
                .andReturn();
        // then 2
        ApiResponse<LoginResponse> response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                API_LOGIN_RESPONSE_TYPE_REFERENCE
        );
        assertThat(response.getData().getAccessToken()).isNotEmpty();
    }

    @Test
    public void 로그인_후_받은_토큰으로__인증이_필요한_API_를_조회하면__잘_응답해야함() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccessToken("kakaoAccessToken");
        TestApiResult<LoginResponse> loginResult = loginControllerApi.login(loginRequest);
        String accessToken = loginResult.getApiResponse().getData().getAccessToken();
        // when
        TestApiResult<MemberResponse> getMeResult = memberControllerApi.getMe(accessToken);
        // then
        assertThat(getMeResult.is2xxSuccessful()).isTrue();

        MemberResponse memberResponse = getMeResult.getApiResponse().getData();
        assertThat(memberResponse).isNotNull();
        assertThat(memberResponse.getId()).isNotNull();
    }

    @Test
    public void 두_번째_로그인하는_경우_처음_로그인했을_때와_같은_멤버로_로그인되어야함() throws Exception {
        // given
        String kakaoAccessToken = "kakaoAccessToken";
        // when
        Long firstMemberId = this.loginAndGetMemberId(kakaoAccessToken);
        Long secondMemberId = this.loginAndGetMemberId(kakaoAccessToken);
        // then
        assertThat(firstMemberId).isEqualTo(secondMemberId);
    }

    private Long loginAndGetMemberId(String kakaoAccessToken) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAccessToken(kakaoAccessToken);

        TestApiResult<LoginResponse> loginResult = loginControllerApi.login(loginRequest);
        String firstAccessToken = loginResult.getApiResponse().getData().getAccessToken();

        TestApiResult<MemberResponse> getMeResult = memberControllerApi.getMe(firstAccessToken);
        return getMeResult.getApiResponse().getData().getId();
    }
}
