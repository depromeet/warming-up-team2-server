package com.depromeet.booboo.integration;

import com.depromeet.booboo.api.LoginControllerApi;
import com.depromeet.booboo.api.MemberControllerApi;
import com.depromeet.booboo.api.TestApiResult;
import com.depromeet.booboo.api.helper.MemberApiHelper;
import com.depromeet.booboo.application.adapter.kakao.KakaoAdapter;
import com.depromeet.booboo.ui.dto.ConnectCoupleRequest;
import com.depromeet.booboo.ui.dto.CoupleResponse;
import com.depromeet.booboo.ui.dto.MemberResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectCoupleTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KakaoAdapter kakaoAdapter;

    private MemberControllerApi memberControllerApi;

    private String accessTokenSoloA;
    private String accessTokenSoloB;
    private String accessTokenCoupleC;
    private String accessTokenCoupleD;

    @Before
    public void setUp() throws Exception {
        memberControllerApi = new MemberControllerApi(mockMvc, objectMapper);

        LoginControllerApi loginControllerApi = new LoginControllerApi(mockMvc, objectMapper);
        MemberApiHelper memberApiHelper = new MemberApiHelper(kakaoAdapter, loginControllerApi);

        // create members (a,b,c,d)
        accessTokenSoloA = memberApiHelper.createOAuthUser("kakaoAccessTokenA", 111L, "soloA", "");
        accessTokenSoloB = memberApiHelper.createOAuthUser("kakaoAccessTokenB", 222L, "soloB", "");
        accessTokenCoupleC = memberApiHelper.createOAuthUser("kakaoAccessTokenC", 333L, "coupleC", "");
        accessTokenCoupleD = memberApiHelper.createOAuthUser("kakaoAccessTokenD", 444L, "coupleD", "");

        // create a couple (c-d)
        TestApiResult<MemberResponse> getMeResult = memberControllerApi.getMe(accessTokenCoupleC);
        String connectionCode = getMeResult.getApiResponse().getData().getConnectionCode();

        ConnectCoupleRequest connectCoupleRequest = new ConnectCoupleRequest();
        connectCoupleRequest.setConnectionCode(connectionCode);
        TestApiResult<CoupleResponse> connectResult = memberControllerApi.connectCouple(accessTokenCoupleD, connectCoupleRequest);
        assertThat(connectResult.is2xxSuccessful()).isTrue();
    }

    @Test
    public void 솔로인_멤버들을_연결하면__성공() throws Exception {
        // given
        String myAccessToken = accessTokenSoloA;
        String yourAccessToken = accessTokenSoloB;
        // when
        TestApiResult<MemberResponse> getMeResult = memberControllerApi.getMe(yourAccessToken);
        assertThat(getMeResult.is2xxSuccessful()).isTrue();
        String connectionCode = getMeResult.getApiResponse().getData().getConnectionCode();

        ConnectCoupleRequest connectCoupleRequest = new ConnectCoupleRequest();
        connectCoupleRequest.setConnectionCode(connectionCode);
        TestApiResult<CoupleResponse> connectResult = memberControllerApi.connectCouple(myAccessToken, connectCoupleRequest);
        // then
        assertThat(connectResult.is2xxSuccessful()).isTrue();
    }

    @Test
    public void 내가_커플인경우_연결요청하면__실패응답() throws Exception {
        // given
        String myAccessToken = accessTokenCoupleC;
        String yourAccessToken = accessTokenSoloB;
        // when
        TestApiResult<MemberResponse> getMeResult = memberControllerApi.getMe(yourAccessToken);
        assertThat(getMeResult.is2xxSuccessful()).isTrue();
        String connectionCode = getMeResult.getApiResponse().getData().getConnectionCode();

        ConnectCoupleRequest connectCoupleRequest = new ConnectCoupleRequest();
        connectCoupleRequest.setConnectionCode(connectionCode);
        TestApiResult<CoupleResponse> connectResult = memberControllerApi.connectCouple(myAccessToken, connectCoupleRequest);
        // then
        assertThat(connectResult.is4xxClientError()).isTrue();
    }

    @Test
    public void 나는_솔로인데_상대가_이미_커플인경우__실패응답() throws Exception {
        // given
        String myAccessToken = accessTokenSoloA;
        String yourAccessToken = accessTokenCoupleC;
        // when
        TestApiResult<MemberResponse> getMeResult = memberControllerApi.getMe(yourAccessToken);
        assertThat(getMeResult.is2xxSuccessful()).isTrue();
        String connectionCode = getMeResult.getApiResponse().getData().getConnectionCode();

        ConnectCoupleRequest connectCoupleRequest = new ConnectCoupleRequest();
        connectCoupleRequest.setConnectionCode(connectionCode);
        TestApiResult<CoupleResponse> connectResult = memberControllerApi.connectCouple(myAccessToken, connectCoupleRequest);
        // then
        assertThat(connectResult.is4xxClientError()).isTrue();
    }

    @Test
    public void 내가_나의_코드를_입력해서_연결하는경우__실패응답() throws Exception {
        // given
        String myAccessToken = accessTokenSoloA;
        String yourAccessToken = accessTokenSoloA;
        // when
        TestApiResult<MemberResponse> getMeResult = memberControllerApi.getMe(yourAccessToken);
        assertThat(getMeResult.is2xxSuccessful()).isTrue();
        String connectionCode = getMeResult.getApiResponse().getData().getConnectionCode();

        ConnectCoupleRequest connectCoupleRequest = new ConnectCoupleRequest();
        connectCoupleRequest.setConnectionCode(connectionCode);
        TestApiResult<CoupleResponse> connectResult = memberControllerApi.connectCouple(myAccessToken, connectCoupleRequest);
        // then
        assertThat(connectResult.is4xxClientError()).isTrue();
    }
}
