package com.pharmfind.pharmacy.service;

import com.pharmfind.api.dto.DocumentDto;
import com.pharmfind.api.dto.KakaoApiResponseDto;
import com.pharmfind.api.service.KakaoAddressSearchService;
import com.pharmfind.direction.entity.Direction;
import com.pharmfind.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacyRecommendationService {
    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;

    public void recommendPharmacyList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtoList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}", address);
            return;
        }
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
        //List<Direction> directionList = directionService.buildDirectionList(documentDto); // 공공기관 데이터이용
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto); // api 데이터이용
        directionService.saveAll(directionList);
    }
}
