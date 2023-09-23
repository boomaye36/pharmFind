package com.pharmfind.pharmacy.controller;

import com.pharmfind.direction.repository.DirectionRepository;
import com.pharmfind.pharmacy.cache.PharmacyRedisTemplateService;
import com.pharmfind.pharmacy.dto.PharmacyDto;
import com.pharmfind.pharmacy.repository.PharmacyRepository;
import com.pharmfind.pharmacy.service.PharmacyRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RestController
@RequiredArgsConstructor
public class PharmacyController {
    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisTemplateService pharmacyRedisTemplateService;
    private final PharmacyRepository pharmacyRepository;
    private final DirectionRepository directionRepository;

    @GetMapping("/redis/save")
    public String save(){
        List<PharmacyDto> pharmacyDtoList = directionRepository.findAll()
                .stream().map(direction -> PharmacyDto.builder()
                        .id(direction.getId())
                        .pharmacyName(direction.getTargetPharmacyName())
                        .pharmacyAddress(direction.getTargetAddress())
                        .latitude(direction.getTargetLatitude())
                        .longitude(direction.getTargetLongitude())
                        .build())
                .collect(Collectors.toList());

        pharmacyDtoList.forEach(pharmacyRedisTemplateService::save);
    return "success";
    }

}
