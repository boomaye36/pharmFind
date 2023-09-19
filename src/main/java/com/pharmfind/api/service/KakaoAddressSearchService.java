package com.pharmfind.api.service;

import com.pharmfind.api.dto.KakaoApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor

public class KakaoAddressSearchService {
    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;
    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;
    public KakaoApiResponseDto requestAddressSearch(String address){
        if (ObjectUtils.isEmpty(address)) return null;
        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "kakaoAK " + kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        //kakao api 호출
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoApiResponseDto.class).getBody();


    }
}