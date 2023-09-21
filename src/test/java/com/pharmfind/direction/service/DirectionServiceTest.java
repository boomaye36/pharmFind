package com.pharmfind.direction.service;

import com.pharmfind.api.dto.DocumentDto;
import com.pharmfind.direction.entity.Direction;
import com.pharmfind.pharmacy.dto.PharmacyDto;
import com.pharmfind.pharmacy.service.PharmacySearchService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest

class DirectionServiceTest {


    private PharmacySearchService pharmacySearchService = mock(PharmacySearchService.class);
    private DirectionService directionService = new DirectionService(pharmacySearchService);
    private  List<PharmacyDto> pharmactDtoList;
    public DirectionServiceTest(@Autowired PharmacySearchService pharmacySearchService,@Autowired DirectionService directionService) {
        this.pharmacySearchService = pharmacySearchService;
        this.directionService = directionService;
    }
//    private static MockedStatic<PharmacySearchService> aMainActivity;
//
//    @BeforeClass
//    public static void setBeforeClass() {
//        aMainActivity = mockStatic(PharmacySearchService.class);
//    }

    @BeforeEach
    void setUp() {
        pharmactDtoList = new ArrayList<>();

        // Adding PharmacyDto objects to the list using add method
        pharmactDtoList.add(PharmacyDto.builder()
                .id(1L)
                .pharmacyName("돌곶이온누리약국")
                .pharmacyAddress("주소1")
                .latitude(37.61040424)
                .longitude(127.0569046)
                .build());

        pharmactDtoList.add(PharmacyDto.builder()
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
        when(pharmacySearchService.searchPharmacyDtoList()).thenReturn(pharmactDtoList);

        List<Direction> result = directionService.buildDirectionList(documentDto);

        Assertions.assertEquals(result.size(), 2);
        Assertions.assertEquals(result.get(0).getTargetPharmacyName(), "호수온누리약국");
        Assertions.assertEquals(result.get(1).getTargetPharmacyName(), "돌곶이온누리약국");

    }

    @Test
    void 정해진_반경_10km_이내_검색(){
        double latitude1 = 37.5505;
        double longitude1 = 127.0817;

        double latitude2 = 37.541;
        double longitude2 = 127.0766;
        String result = "1.1";
    }
//    @AfterClass
//    public static void setAfterClass() {
//        aMainActivity.close();
//    }
}