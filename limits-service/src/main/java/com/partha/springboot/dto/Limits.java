package com.partha.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Limits {
    private Integer min;
    private Integer max;
}
