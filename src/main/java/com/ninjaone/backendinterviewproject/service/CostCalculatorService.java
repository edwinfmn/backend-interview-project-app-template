package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.ServRepository;
import com.ninjaone.backendinterviewproject.model.Serv;
import com.ninjaone.backendinterviewproject.model.ServiceAssign;
import com.ninjaone.backendinterviewproject.response.CostDetail;
import com.ninjaone.backendinterviewproject.response.CostSummary;
import com.ninjaone.backendinterviewproject.service.ServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CostCalculatorService {

    private ServRepository servRepository;

    @Value("${service.default-device-name}")
    private String deviceServiceName;


    public CostCalculatorService(ServRepository servRepository) {
        this.servRepository = servRepository;
    }

    public CostSummary calculateTotalCost(List<ServiceAssign> list, BigDecimal costByDevice) {
        AtomicReference<BigDecimal> grandTotal = new AtomicReference<>(BigDecimal.ZERO);
        List<CostDetail> details = new ArrayList<>();
        CostSummary total = new CostSummary();

        // calculate Device cost for all Devices
        grandTotal.set(calculateDevicesCost(list, costByDevice).add(grandTotal.get()));
        CostDetail cd = new CostDetail(deviceServiceName, grandTotal.get());
        if (deviceServiceName != null) {
            details.add(cd);
        }

        list.stream().forEach(s -> {
            CostDetail temp = populateDetails(details, s);
            grandTotal.set(s.getService().getPrice().add(grandTotal.get()));
            details.add(temp);
        });

        total.setExplanation(details);
        total.setTotalCost(grandTotal.get());
        return total;

    }

    public CostSummary calculateTotalCost(List<ServiceAssign> list) {
        AtomicReference<BigDecimal> grandTotal = new AtomicReference<>(BigDecimal.ZERO);
        BigDecimal costByDevice = BigDecimal.ZERO;
        List<CostDetail> details = new ArrayList<>();
        CostSummary total = new CostSummary();

        // get Default Costs by Device
        Optional<Serv> optionalServ = servRepository.searchByName(deviceServiceName).stream().findFirst();
        if(optionalServ.isPresent())
            costByDevice = optionalServ.get().getPrice();
        // calculate Device cost for all Devices
        grandTotal.set(calculateDevicesCost(list, costByDevice).add(grandTotal.get()));
        CostDetail cd = new CostDetail(deviceServiceName, grandTotal.get());
        if (deviceServiceName != null) {
            details.add(cd);
        }

        list.stream().forEach(s -> {
          CostDetail temp = populateDetails(details, s);
          grandTotal.set(s.getService().getPrice().add(grandTotal.get()));
          details.add(temp);
        });

        total.setExplanation(details);
        total.setTotalCost(grandTotal.get());
        return total;

    }

    private BigDecimal calculateDevicesCost(List<ServiceAssign> list, BigDecimal costByDevice) {
        return new BigDecimal(list.stream().map(ServiceAssign::getDevice).distinct().count()).multiply(costByDevice);
    }

    private CostDetail populateDetails(List<CostDetail> details, ServiceAssign s) {
        CostDetail detail = details.stream().filter(d -> d.getService().equalsIgnoreCase(s.getService().getServiceName())).findAny().orElse(null);
        if(detail != null) {
            details.remove(detail);
            detail.setCost(detail.getCost().add(s.getService().getPrice()));
        } else {
            detail = new CostDetail(s.getService().getServiceName(), s.getService().getPrice() );
        }
        return detail;
    }

    public String getDeviceServiceName() {
        return deviceServiceName;
    }

    public void setDeviceServiceName(String deviceServiceName) {
        this.deviceServiceName = deviceServiceName;
    }
}
