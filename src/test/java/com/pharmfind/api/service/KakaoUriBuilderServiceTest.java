package com.pharmfind.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;

@SpringBootTest

class KakaoUriBuilderServiceTest {
    private KakaoUriBuilderService kakaoUriBuilderService;

    KakaoUriBuilderServiceTest(@Autowired KakaoUriBuilderService kakaoUriBuilderService) {
        this.kakaoUriBuilderService = kakaoUriBuilderService;
    }

    @Test
    void buildUriByAddressSearch() {
        String address = "서울 강남구";
        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);
        System.out.println(uri);
        String decodeResult = URLDecoder.decode(uri.toString(), Charset.defaultCharset());
        System.out.println("decode 결과값" + decodeResult);
    }
}