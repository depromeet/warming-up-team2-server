package com.depromeet.booboo.infrastructure.adapter.kakao;

import com.depromeet.booboo.application.adapter.kakao.KakaoAdapter;
import com.depromeet.booboo.application.adapter.kakao.KakaoApiFailedException;
import com.depromeet.booboo.application.adapter.kakao.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class KakaoAdapterImpl implements KakaoAdapter {
    private final RestTemplate restTemplate;

    @Override
    public KakaoUserResponse getUserInfo(String accessToken) {
        Assert.notNull(accessToken, "'accessToken' must not be null");

        final URI requestUrl = UriComponentsBuilder.fromHttpUrl("https://kapi.kakao.com/v2/user/me")
                .build(true)
                .toUri();

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        final HttpEntity httpEntity = new HttpEntity(httpHeaders);

        final ResponseEntity<KakaoUserResponse> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, KakaoUserResponse.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new KakaoApiFailedException("Failed to get User details from kakao api. status:" + responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }
}