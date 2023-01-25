package com.ninjaone.backendinterviewproject.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CostSummary {

    private BigDecimal totalCost;
    private List<CostDetail> explanation;

    public CostSummary() {
        this.totalCost = BigDecimal.ZERO;
        this.explanation = new ArrayList<CostDetail>();
    }

    public CostSummary(BigDecimal totalCost, List<CostDetail> explanation) {
        this.totalCost = totalCost;
        this.explanation = explanation;
    }
}
