package com.partha.springboot.controller;

import com.partha.springboot.dto.Limits;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {

    @Value("${limits-service.min}")
    private Integer min;
    @Value("${limits-service.max}")
    private Integer max;

    @GetMapping("/limits")
    public Limits getLimits(){
        return new Limits(min,max);
    }
}
