package com.pharmfind.pharmacy.repository;

import com.pharmfind.AbstractIntegrationContainerBaseTest;
import com.pharmfind.pharmacy.entity.Pharmacy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PharmacyRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    public PharmacyRepositoryTest(@Autowired PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    @BeforeEach
    void delete() {
        pharmacyRepository.deleteAll();
    }

    @Test
    void 기본적인CRUD() {
        String address = "서울 특별시 성북구 중암동";
        String name = "은혜 약국";
        double latitude = 36.11;
        double logitude = 128.11;

        Pharmacy pharmacy = Pharmacy.builder()
                .pharmacyAddress(address)
                .pharmacyName(name)
                .latitude(latitude)
                .longitude(logitude).build();
        Pharmacy result = pharmacyRepository.save(pharmacy);
        Assertions.assertEquals(result.getPharmacyName(), name);
    }

    @Test
    void saveAll() {
        String address = "서울 특별시 성북구 중암동";
        String name = "은혜 약국";
        double latitude = 36.11;
        double logitude = 128.11;

        Pharmacy pharmacy = Pharmacy.builder()
                .pharmacyAddress(address)
                .pharmacyName(name)
                .latitude(latitude)
                .longitude(logitude).build();
        pharmacyRepository.saveAll(Arrays.asList(pharmacy));
        List<Pharmacy> resultList = pharmacyRepository.findAll();
        Assertions.assertEquals(resultList.size(), 1);
    }
}