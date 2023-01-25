package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.ServRepository;
import com.ninjaone.backendinterviewproject.database.ServiceAssignRepository;
import com.ninjaone.backendinterviewproject.model.Serv;
import com.ninjaone.backendinterviewproject.model.ServiceAssign;
import com.ninjaone.backendinterviewproject.response.CostSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceAssignService {

    private final ServiceAssignRepository serviceAssignRepository;

    @Autowired
    private ServRepository serviceRepository;

    @Autowired
    private CostCalculatorService costCalculatorService;

    @Value("${service.default-device-name}")
    private String deviceServiceName;

    private CostSummary totalCost = new CostSummary();

    public ServiceAssignService(ServiceAssignRepository serviceAssignRepository) {
        this.serviceAssignRepository = serviceAssignRepository;
    }

    /**
     * Create a new service assignment, performing the cost calculation when a new service is assigned to a device
     * @param serviceAssign
     * @return ServiceAssign object that is already created on database.
     */
    public ServiceAssign saveServiceByDevice(ServiceAssign serviceAssign) {
        ServiceAssign assign = serviceAssignRepository.save(serviceAssign);
        BigDecimal costByDevice = BigDecimal.ZERO;
        Optional<Serv> optionalServ = serviceRepository.searchByName(deviceServiceName).stream().findFirst();
        if(optionalServ.isPresent())
            costByDevice = optionalServ.get().getPrice();

        setTotalCost(costCalculatorService.calculateTotalCost(listAllServicesAssignments(), costByDevice));
        return assign;
    }

    public List<ServiceAssign> listAllServicesAssignments() { return (List<ServiceAssign>) serviceAssignRepository.findAll(); }

    public Optional<ServiceAssign> getServiceAssignmentById(String id){
        return serviceAssignRepository.findById(id);
    }

    public void deleteById(String id) {
        serviceAssignRepository.deleteById(id);
    }

    public String getDeviceServiceName() {
        return deviceServiceName;
    }

    public void setDeviceServiceName(String deviceServiceName) {
        this.deviceServiceName = deviceServiceName;
    }

    public CostSummary getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(CostSummary totalCost) {
        this.totalCost = totalCost;
    }

}
