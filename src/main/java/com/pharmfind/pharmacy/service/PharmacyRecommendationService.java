package com.pharmfind.pharmacy.service;

import com.pharmfind.api.dto.DocumentDto;
import com.pharmfind.api.dto.KakaoApiResponseDto;
import com.pharmfind.api.service.KakaoAddressSearchService;
import com.pharmfind.direction.dto.OutputDto;
import com.pharmfind.direction.entity.Direction;
import com.pharmfind.direction.service.Base62Service;
import com.pharmfind.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.result.Output;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacyRecommendationService {
    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    private final Base62Service base62Service;
    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
   // private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";

    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;
    public List<OutputDto> recommendPharmacyList(String address) {
        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentDtoList())) {
            log.error("[PharmacyRecommendationService recommendPharmacyList fail] Input address: {}", address);
            return Collections.emptyList();
        }
        DocumentDto documentDto = kakaoApiResponseDto.getDocumentDtoList().get(0);
        //List<Direction> directionList = directionService.buildDirectionList(documentDto); // 공공기관 데이터이용
        List<Direction> directionList = directionService.buildDirectionListByCategoryApi(documentDto); // api 데이터이용
        return directionService.saveAll(directionList)
                .stream()
                .map(t -> convertToOutputDto(t))
                .collect(Collectors.toList());
    }


    private OutputDto convertToOutputDto(Direction direction){
//        String param = String.join(",", direction.getTargetPharmacyName(),
//                String.valueOf(direction.getTargetLatitude()),
//                String.valueOf(direction.getTargetLongitude()));
//        String directionUrl = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + param)
//                .toUriString();
//
//        log.info("direction param : {}, url : {}", param, directionUrl);
        return OutputDto.builder()
                .pharmacyAddress(direction.getTargetAddress())
                .pharmacyName(direction.getTargetPharmacyName())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId())) // TODO
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() +
                        "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))
                .build();
    }
}
