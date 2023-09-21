package com.pharmfind.direction.service;

import com.pharmfind.api.dto.DocumentDto;
import com.pharmfind.direction.entity.Direction;
import com.pharmfind.direction.repository.DirectionRepository;
import com.pharmfind.pharmacy.dto.PharmacyDto;
import com.pharmfind.pharmacy.service.PharmacySearchService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest

class DirectionServiceTest {
    @Mock
    private PharmacySearchService pharmacySearchService;

    private DirectionService directionService;
    private DirectionRepository directionRepository;

    private List<PharmacyDto> pharmacyList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        directionService = new DirectionService(
                directionRepository,
                pharmacySearchService);

        pharmacyList = new ArrayList<>();
        // Adding PharmacyDto objects to the list using add method
        pharmacyList.add(PharmacyDto.builder()
                .id(1L)
                .pharmacyName("돌곶이온누리약국")
                .pharmacyAddress("주소1")
                .latitude(37.61040424)
                .longitude(127.0569046)
                .build());

        pharmacyList.add(PharmacyDto.builder()
                .id(2L)
                .pharmacyName("호수온누리약국")
                .pharmacyAddress("주소2")
                .latitude(37.60894036)
                .longitude(127.029052)
                .build());
    }

    @Test
    void 결과값이_거리_순으로_정렬() {
        String addressName = "서울 성북구 종암로10길";
        double inputLatitude = 37.5960650456809;
        double inputLongitude = 127.037033003036;

        DocumentDto documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build();
        when(pharmacySearchService.searchPharmacyDtoList()).thenReturn(pharmacyList);

        List<Direction> result = directionService.buildDirectionList(documentDto);

        Assertions.assertEquals(result.size(), 2);
        Assertions.assertEquals(result.get(0).getTargetPharmacyName(), "호수온누리약국");
        Assertions.assertEquals(result.get(1).getTargetPharmacyName(), "돌곶이온누리약국");

    }

    @Test
    void 정해진_반경_10km_이내_검색() {
        pharmacyList.add(PharmacyDto.builder()
                .id(3L)
                .pharmacyName("경기약국")
                .pharmacyAddress("주소3")
                .latitude(37.3825107393401)
                .longitude(127.236707811313)
                .build());

        String addressName = "서울 성북구 종암로10길";
        double inputLatitude = 37.5960650456809;
        double inputLongitude = 127.037033003036;

        DocumentDto documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build();

        // When
        when(pharmacySearchService.searchPharmacyDtoList()).thenReturn(pharmacyList);

        List<Direction> results = directionService.buildDirectionList(documentDto);

        // Then
        assertEquals(2, results.size());
        assertEquals("호수온누리약국", results.get(0).getTargetPharmacyName());
        assertEquals("돌곶이온누리약국", results.get(1).getTargetPharmacyName());
    }

}