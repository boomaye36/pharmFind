package com.pharmfind.direction.controller;

import com.pharmfind.direction.entity.Direction;
import com.pharmfind.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";
    private final DirectionService directionService;

    @GetMapping("/dir/{encodedId}")
    public String searchDirection(@PathVariable("encodedId") String encodedId){
        Direction resultDirection = directionService.findById(encodedId);

        String param = String.join(",", resultDirection.getTargetPharmacyName(),
                String.valueOf(resultDirection.getTargetLatitude()),
                String.valueOf(resultDirection.getTargetLongitude()));
        String directionUrl = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + param)
                .toUriString();

        log.info("direction param : {}, url : {}", param, directionUrl);
        return "redirect:" + directionUrl;
    }

}
