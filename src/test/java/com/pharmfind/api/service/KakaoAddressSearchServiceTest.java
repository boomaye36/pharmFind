package com.pharmfind.api.service;

import com.pharmfind.AbstractIntegrationContainerBaseTest;
import com.pharmfind.api.dto.KakaoApiResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class KakaoAddressSearchServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private KakaoAddressSearchService kakaoAddressSearchService;

    public KakaoAddressSearchServiceTest(@Autowired KakaoAddressSearchService kakaoAddressSearchService) {
        this.kakaoAddressSearchService = kakaoAddressSearchService;
    }

    @Test
    void nullTest(){
        String address = null;
        Assertions.assertEquals(kakaoAddressSearchService.requestAddressSearch(null), null);
    }

    @Test
    void requestAddressSearch() {
        String address = "서울 성북구 종암로 10길";
        KakaoApiResponseDto result = kakaoAddressSearchService.requestAddressSearch(address);
        Assertions.assertTrue(result.getDocumentDtoList().size() > 0);
        Assertions.assertTrue(result.getMetaDto().getTotalCount() > 0);
        Assertions.assertNotEquals(result.getDocumentDtoList().get(0), null);
    }
}