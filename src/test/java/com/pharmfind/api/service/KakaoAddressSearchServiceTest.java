package com.pharmfind.api.service;

import com.pharmfind.AbstractIntegrationContainerBaseTest;
import com.pharmfind.api.dto.DocumentDto;
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
    void requestAddressSearch() {
        String address = "서울 성북구 종암로 10길";
        KakaoApiResponseDto result = kakaoAddressSearchService.requestAddressSearch(address);
        Assertions.assertTrue(result.getDocumentDtoList().size() > 0);
        Assertions.assertTrue(result.getMetaDto().getTotalCount() > 0);
        Assertions.assertNotEquals(result.getDocumentDtoList().get(0), null);
    }

    @Test
    void 정상적인_주소를_입력했을_경우_정상적으로_위도_경도로_반환_한다(){
        boolean actualResult;

        // When & Then
        actualResult = checkAddress("서울 특별시 성북구 종암동", true);
        assertEquals(true, actualResult);

        actualResult = checkAddress("서울 성북구 종암동 91", true);
        assertEquals(true, actualResult);

        actualResult = checkAddress("서울 대학로", true);
        assertEquals(true, actualResult);

        actualResult = checkAddress("서울 성북구 종암동 잘못된 주소", false);
        assertEquals(false, actualResult);

        actualResult = checkAddress("광진구 구의동 251-45", true);
        assertEquals(true, actualResult);

        actualResult = checkAddress("광진구 구의동 251-455555", false);
        assertEquals(false, actualResult);
//
        actualResult = checkAddress("", false);
        assertEquals(false, actualResult);
    }
    private boolean checkAddress(String inputAddress, boolean expectedResult) {
        KakaoApiResponseDto searchResult = kakaoAddressSearchService.requestAddressSearch(inputAddress);
        return (searchResult != null && searchResult.getDocumentDtoList().size() > 0);
    }
}