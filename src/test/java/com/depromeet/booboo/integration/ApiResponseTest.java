package com.depromeet.booboo.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiResponseTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void dto_를_json_으로_잘_응답하는지() throws Exception {
        mockMvc.perform(get("/members/1"))
                .andDo(print())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.totalCount").exists())
                .andExpect(jsonPath("$.data").exists());
    }
}
