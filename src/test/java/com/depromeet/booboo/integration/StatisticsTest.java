package com.depromeet.booboo.integration;

import com.depromeet.booboo.api.ExpenditureControllerApi;
import com.depromeet.booboo.api.LoginControllerApi;
import com.depromeet.booboo.api.StatisticsControllerApi;
import com.depromeet.booboo.api.TestApiResult;
import com.depromeet.booboo.api.helper.MemberApiHelper;
import com.depromeet.booboo.application.adapter.kakao.KakaoAdapter;
import com.depromeet.booboo.domain.category.Category;
import com.depromeet.booboo.ui.dto.*;
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

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class StatisticsTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KakaoAdapter kakaoAdapter;

    private ExpenditureControllerApi expenditureControllerApi;
    private StatisticsControllerApi statisticsControllerApi;

    private String accessToken;

    @Before
    public void setUp() throws Exception {
        expenditureControllerApi = new ExpenditureControllerApi(mockMvc, objectMapper);
        statisticsControllerApi = new StatisticsControllerApi(mockMvc, objectMapper);

        LoginControllerApi loginControllerApi = new LoginControllerApi(mockMvc, objectMapper);
        MemberApiHelper memberApiHelper = new MemberApiHelper(kakaoAdapter, loginControllerApi);
        accessToken = memberApiHelper.createOAuthUser("kakaoAccessToken", 111L, "soloA", "");

        this.createExpenditure(1000L,
                Category.DefaultCategories.SUPPLIES.value(),
                LocalDate.of(2019, 6, 1));
        this.createExpenditure(2000L,
                Category.DefaultCategories.SUPPLIES.value(),
                LocalDate.of(2019, 7, 1));
        this.createExpenditure(3000L,
                Category.DefaultCategories.BABY_PRODUCTS.value(),
                LocalDate.of(2019, 8, 1));
        this.createExpenditure(4000L,
                Category.DefaultCategories.BABY_PRODUCTS.value(),
                LocalDate.of(2019, 9, 1));
        this.createExpenditure(6000L,
                Category.DefaultCategories.HEALTH.value(),
                LocalDate.of(2019, 11, 1));
        this.createExpenditure(7000L,
                Category.DefaultCategories.HEALTH.value(),
                LocalDate.of(2019, 11, 1));
        this.createExpenditure(8000L,
                Category.DefaultCategories.HEALTH.value(),
                LocalDate.of(2019, 11, 2));
        this.createExpenditure(9000L,
                Category.DefaultCategories.HEALTH.value(),
                LocalDate.of(2019, 11, 3));
        this.createExpenditure(10000L,
                Category.DefaultCategories.HEALTH.value(),
                LocalDate.of(2019, 11, 4));
    }

    @Test
    public void 카테고리_그래프_테스트() throws Exception {
        // given
        // when
        TestApiResult<MostExpendedCategoryResponse> categoriesResult = statisticsControllerApi.getCategoriesMostExpended(accessToken);
        // then
        assertThat(categoriesResult.is2xxSuccessful()).isTrue();
    }

    private void createExpenditure(Long amountOfMoney, String category, LocalDate localDate) throws Exception {
        ExpenditureRequest expenditureRequest = new ExpenditureRequest();
        expenditureRequest.setAmountOfMoney(amountOfMoney);
        expenditureRequest.setTitle("title");
        expenditureRequest.setCategory(category);
        expenditureRequest.setExpendedAt(localDate);
        TestApiResult<ExpenditureResponse> createResult = expenditureControllerApi.createExpenditure(accessToken, expenditureRequest);
        assertThat(createResult.is2xxSuccessful()).isTrue();
    }

    @Test
    public void 월별_지출_합계_그래프_테스트() throws Exception {
        // given
        // when
        TestApiResult<MonthlyTotalExpenditureResponse> expendituresResult = statisticsControllerApi.getExpendituresMonthly(accessToken);
        // then
        assertThat(expendituresResult.is2xxSuccessful()).isTrue();
    }

    @Test
    public void 일별_지출_합계_그래프_테스트() throws Exception {
        // given
        // when
        TestApiResult<Map<String, MonthlyExpenditureResponse>> dailyExpendituresResult = statisticsControllerApi.getExpendituresDaily(accessToken);
        // then
        assertThat(dailyExpendituresResult.is2xxSuccessful()).isTrue();
    }
}
