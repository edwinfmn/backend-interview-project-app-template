package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.model.Device;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private Map<String, BigDecimal> deviceCosts = new HashMap<>();

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device saveDevice(Device device){
        return deviceRepository.save(device);
    }

    public List<Device> listAllDevices() { return (List<Device>) deviceRepository.findAll(); }

    public Optional<Device> getDeviceById(String id){
        return deviceRepository.findById(id);
    }

    public void deleteById(String id) {
        deviceRepository.deleteById(id);
    }

    public List<Device> searchByName(String name) {
        return deviceRepository.searchByName(name);
    }

    public Map<String, BigDecimal> getDeviceCosts() {
        return deviceCosts;
    }

    public void setDeviceCosts(Map<String, BigDecimal> deviceCosts) {
        this.deviceCosts = deviceCosts;
    }
}
