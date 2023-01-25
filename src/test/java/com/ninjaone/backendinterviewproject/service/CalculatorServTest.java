package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.ServiceAssignRepository;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.Serv;
import com.ninjaone.backendinterviewproject.model.ServiceAssign;
import com.ninjaone.backendinterviewproject.response.CostSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CalculatorServTest {
    public static final String ID = "12345";

    @Mock
    private ServiceAssignRepository testRepository;

    @InjectMocks
    private CostCalculatorService testObject;

    private CostSummary summaryEntity;

    @BeforeEach
    void setup(){
        summaryEntity = new CostSummary();
    }

    @Test
    void calculateCostData() {
        Device device = new Device("5", "WINDOWS", "Windows Server");
        Serv service = new Serv("9", "Device", "ANY", new BigDecimal(5));
        Serv service2 = new Serv("8", "Office License", "ANY", new BigDecimal(7));
        ServiceAssign sa1 = new ServiceAssign("20", device, service);
        ServiceAssign sa2 = new ServiceAssign("21", device, service2);

        List<ServiceAssign> list = new ArrayList<>();
        list.add(sa1); list.add(sa2);

        CostSummary costResult = testObject.calculateTotalCost(list, new BigDecimal(5));

        assert costResult != null;
        assertEquals(costResult.getTotalCost(), new BigDecimal(17));
        assertEquals(costResult.getExplanation().size(), 2);
    }


}

