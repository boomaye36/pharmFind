package com.pharmfind.pharmacy.service;

import com.pharmfind.pharmacy.entity.Pharmacy;
import com.pharmfind.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacrRepositoryService {
    private final PharmacyRepository pharmacyRepository;
    public void updateAddressWithoutTransaction(Long id, String address){
        Pharmacy pharmacy = pharmacyRepository.findById(id).orElse(null);
        if (Objects.isNull(pharmacy)){
            log.error("약국정보가 없습니다. id : {}", id);
            return;
        }
        pharmacy.setPharmacyAddress(address);
    }
}
