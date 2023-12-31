package com.pharmfind.direction.service;

import com.pharmfind.api.dto.DocumentDto;
import com.pharmfind.api.service.KakaoCategorySearchService;
import com.pharmfind.direction.entity.Direction;
import com.pharmfind.direction.repository.DirectionRepository;
import com.pharmfind.pharmacy.dto.PharmacyDto;
import com.pharmfind.pharmacy.service.PharmacySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";
    private final DirectionRepository directionRepository;
    private  static final int MAX_SEARCH_COUNT = 3;// 약국 최대 검색 갯수
    private  static final double RADIUS_KM = 10.0;// 반경 10 KM 이내
    private final Base62Service base62Service;
    private final PharmacySearchService pharmacySearchService;
    private final KakaoCategorySearchService kakaoCategorySearchService;


    @Transactional
    public List<Direction> saveAll(List<Direction> directionList){
        if(CollectionUtils.isEmpty(directionList)) return Collections.emptyList();
        return directionRepository.saveAll(directionList);

    }

    public String findShortenUrlById(String encodedId){
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction = directionRepository.findById(decodedId).orElse(null);
        String param = String.join(",", direction.getTargetPharmacyName(),
                String.valueOf(direction.getTargetLatitude()),
                String.valueOf(direction.getTargetLongitude()));
        String directionUrl = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + param)
                .toUriString();

        log.info("direction param : {}, url : {}", param, directionUrl);
        return directionUrl;
    }
    public List<Direction> buildDirectionList(DocumentDto documentDto){
        /**
         *  1.약국 데이터 조회
         *  2.거리 계산 알고리즘을 이용하여 고객과 약국 사이의 거리를 계산하고 sort
         */

        if (Objects.isNull(documentDto)) return Collections.emptyList();
        return pharmacySearchService.searchPharmacyDtoList()
                .stream().map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLongitude(documentDto.getLongitude())
                                .inputLatitude(documentDto.getLatitude())
                                .targetPharmacyName(pharmacyDto.getPharmacyName())
                                .targetAddress(pharmacyDto.getPharmacyAddress())
                                .targetLatitude(pharmacyDto.getLatitude())
                                .targetLongitude(pharmacyDto.getLongitude())
                                .distance(
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                                pharmacyDto.getLatitude(), pharmacyDto.getLongitude()
                                                )
                                )
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }


    public List<Direction> buildDirectionListByCategoryApi(DocumentDto documentDto){

        if (Objects.isNull(documentDto)) return Collections.emptyList();
        List<PharmacyDto> redisList = kakaoCategorySearchService.getRedisList();
        if(!redisList.isEmpty())  return redisList.stream().map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLongitude(documentDto.getLongitude())
                                .inputLatitude(documentDto.getLatitude())
                                .targetPharmacyName(pharmacyDto.getPharmacyName())
                                .targetAddress(pharmacyDto.getPharmacyAddress())
                                .targetLatitude(pharmacyDto.getLatitude())
                                .targetLongitude(pharmacyDto.getLongitude())
                                .distance(
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                                pharmacyDto.getLatitude(), pharmacyDto.getLongitude()
                                        )
                                )
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
        return kakaoCategorySearchService.requestPharmacyCategorySearch(documentDto.getLatitude(),
                        documentDto.getLongitude(),
                        RADIUS_KM).getDocumentDtoList()
                .stream().map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLongitude(documentDto.getLongitude())
                                .inputLatitude(documentDto.getLatitude())
                                .targetPharmacyName(pharmacyDto.getPlaceName())
                                .targetAddress(pharmacyDto.getAddressName())
                                .targetLatitude(pharmacyDto.getLatitude())
                                .targetLongitude(pharmacyDto.getLongitude())
                                .distance(
                                        pharmacyDto.getDistance() * 0.001 // km 단위
                                )
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }



    // Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
