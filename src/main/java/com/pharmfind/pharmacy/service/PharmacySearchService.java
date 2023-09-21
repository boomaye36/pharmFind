package com.pharmfind.pharmacy.service;

import com.pharmfind.pharmacy.dto.PharmacyDto;
import com.pharmfind.pharmacy.entity.Pharmacy;
import com.pharmfind.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class PharmacySearchService {
    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRepository pharmacyRepository;

    public List<PharmacyDto> searchPharmacyDtoList(){

        //db
       return pharmacyRepository.findAll()
               .stream()
               .map(this::convertToPharmacyDto)
               .collect(Collectors.toList());
    }

    private PharmacyDto convertToPharmacyDto(Pharmacy pharmacy){
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude()).build();
    }
}
