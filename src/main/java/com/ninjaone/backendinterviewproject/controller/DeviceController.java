package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.response.ResponseObject;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "Device Controller", value = "Device Controller")
@RequestMapping("/device")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @ApiOperation(value = "Get a list of all Devices", notes = "Get a list of all devices registered on database.")
    @GetMapping("/all")
    private List<Device> getAllDevices() {
        return deviceService.listAllDevices();
    }

    @ApiOperation(value = "Find a device by Id", notes = "Get a device by the unique identifier, shows a message when the device doesn't exist.")
    @GetMapping("/{id}")
    private ResponseObject<Device> getDeviceById(
            @ApiParam(value = "Unique identifier of the device")
            @PathVariable String id) {
        try {
            return new ResponseObject<>(HttpStatus.OK, null, deviceService.getDeviceById(id).orElseThrow());
        } catch (Exception e) {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Device ID -" + id + "- doesn't exists", null);
        }
    }

    @ApiOperation(value = "Find all devices that match the System Name", notes = "Get a list of devices as result of a search by System Name, it shows an empty list when no devices are found.")
    @GetMapping("/name/{name}")
    private List<Device> searchBySystemName(
            @ApiParam(value = "System Name of the device")
            @PathVariable String name) {
        return deviceService.searchByName(name);
    }

    @ApiOperation(value = "Create a new device", notes = "Creates a new device in the system, shows an error message when the device already exists, duplicated devices are not allowed.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created device"),
            @ApiResponse(code = 400, message = "Device repeated or ID already exists")
    })
    @PostMapping
    private ResponseObject<Device> saveDevice(
            @ApiParam(value = "Device information for creating a new device")
            @RequestBody Device device) {
        boolean valid = deviceService.getDeviceById(device.getId()).isEmpty();

        device.setSystemName(device.getSystemName().toUpperCase().trim());
        device.setType(device.getType().trim());

        if(deviceService.searchByName(device.getSystemName()).stream().anyMatch(d -> d.equals(device)))
            valid = false;

        if(valid)
            return new ResponseObject<>(HttpStatus.CREATED, "Device created successfully", deviceService.saveDevice(device));
        else
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "ERROR - Device already exists", null);
    }

    @ApiOperation(value = "Delete a device from system", notes = "Deletes a device in the system, shows an error message when the device doesn't exist.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted device"),
            @ApiResponse(code = 400, message = "Device ID doesn't exist")
    })
    @DeleteMapping(value = "/{id}")
    private ResponseObject<String> deleteById(
            @ApiParam(value = "Unique identifier of the device")
            @PathVariable String id) {
        if (deviceService.getDeviceById(id).isPresent()) {
            deviceService.deleteById(id);
            return new ResponseObject<>(HttpStatus.OK, "Device deleted succesfully", null);
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Device ID " + id + " doesn't exists", null);
        }
    }
}
