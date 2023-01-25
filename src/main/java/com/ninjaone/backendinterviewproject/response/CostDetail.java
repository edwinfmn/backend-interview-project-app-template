package com.ninjaone.backendinterviewproject.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CostDetail {

    private String service;
    private BigDecimal cost;

    public CostDetail() {}

    public CostDetail(String service, BigDecimal cost) {
        this.service = service;
        this.cost = cost;
    }
}
