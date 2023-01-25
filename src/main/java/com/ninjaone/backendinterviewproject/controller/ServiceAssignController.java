package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.model.ServiceAssign;
import com.ninjaone.backendinterviewproject.response.CostSummary;
import com.ninjaone.backendinterviewproject.response.ResponseObject;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import com.ninjaone.backendinterviewproject.service.ServService;
import com.ninjaone.backendinterviewproject.service.ServiceAssignService;
import com.ninjaone.backendinterviewproject.service.CostCalculatorService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Api(tags = "Service assignation to a Device Controller", value = "Service assignation to a Device")
@RequestMapping("/servicebydevice")
public class ServiceAssignController {

    private final ServiceAssignService serviceAssignService;
    private final ServService servService;
    private final DeviceService deviceService;

    @Autowired
    private CostCalculatorService costCalculatorService;

    public ServiceAssignController(ServiceAssignService serviceAssignService, ServService servService, DeviceService deviceService) {
        this.serviceAssignService = serviceAssignService;
        this.servService = servService;
        this.deviceService = deviceService;
    }

    @ApiOperation(value = "Get a list of all Devices and their assigned Services", notes = "Get a list of all devices and assigned services registered on database.")
    @GetMapping("/all")
    private List<ServiceAssign> getAllServiceAssignments() {
        return serviceAssignService.listAllServicesAssignments();
    }

    @ApiOperation(value = "Find a service assignment to a device by Id", notes = "Get a service assignment to a device by the unique identifier, shows a message when the service doesn't exist.")
    @GetMapping("/{id}")
    private ServiceAssign getServiceAssignmentById(
            @ApiParam(value = "Unique identifier of the service assignment to a device to retrieve")
            @PathVariable String id) {
        return serviceAssignService.getServiceAssignmentById(id).orElseThrow();
    }

    @ApiOperation(value = "Create a new service assignment to a device", notes = "Creates a new service assignment to a device in the system, shows an error message when trying to assign an incorrect System Type Service to a Device.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created service assignment to a device"),
            @ApiResponse(code = 400, message = "Service assignment to a device ID already exists. " + "<br>"
                    + "Or Device information is wrong." + "<br>"
                    + "Or Service information ir wrong." + "<br>"
                    + "Or trying to assign a wrong System Type Service to a Device.")
    })
    @PostMapping
    private ResponseObject<ServiceAssign> saveServiceByDevice(
            @ApiParam(value = "Service Assign to Device information for creating a new assignment")
            @RequestBody ServiceAssign serviceAssign) {
        boolean valid = serviceAssignService.getServiceAssignmentById(serviceAssign.getId()).isEmpty();

        /*Validation - Check if device exists to show right message*/
        if(deviceService.getDeviceById(serviceAssign.getDevice().getId()).isEmpty())
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "ERROR - Device ID " + serviceAssign.getDevice().getId() + " doesn't exist", null);

        /*Validation - Check if service exists to show right message*/
        if(servService.getServiceById((serviceAssign.getService().getId())).isEmpty())
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "ERROR - Service ID " + serviceAssign.getService().getId() + " doesn't exist", null);

        /*Validation - Check if Service Assign to right Device Type*/
        String deviceSystemName = deviceService.getDeviceById(serviceAssign.getDevice().getId()).get().getSystemName();
        String serviceType = servService.getServiceById(serviceAssign.getService().getId()).get().getType();
        if( !serviceType.equals("ANY") )
            if ( !deviceSystemName.equals(serviceType) )
                return new ResponseObject<>(HttpStatus.BAD_REQUEST, "ERROR - Service Type " + serviceType +
                        " can't be assigned to Device of Type " + deviceSystemName, null);

        if(valid)
            return new ResponseObject<>(HttpStatus.CREATED, "Service assignment created successfully", serviceAssignService.saveServiceByDevice(serviceAssign));
        else
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "ERROR - Service assignment edit not allowed", null);
    }

    @ApiOperation(value = "Calculate total cost of all services assigned to all devices. ✭ Cached Result ✭", notes = "Creates a new service assignment to a device in the system, shows an error message when trying to assign an incorrect System Type Service to a Device.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Shows the Total Cost calculated for all services assigned to all devices. <br>" +
                    "This operation uses a rudimentary caching - based on a ✭ Simple Service Variable ✭. <br>" +
                    "The ✭ Simple Service Variable ✭ is updated every time a new service is assigned to a device"),
    })
    @GetMapping("/total")
    private ResponseObject<CostSummary> getServiceAssignmentsTotalCost() {

        // get Total from 'cache'
        CostSummary costSummary = serviceAssignService.getTotalCost();

        // if TOTAL is not available from 'cache' then go and perform calculation
        if(!deviceService.listAllDevices().isEmpty() && costSummary.getTotalCost().compareTo(BigDecimal.ZERO) == 0)
            return new ResponseObject<>(HttpStatus.OK, "Services assignment summary", costCalculatorService.calculateTotalCost(getAllServiceAssignments()));
        // else return info automatically from cache
        else
            return new ResponseObject<>(HttpStatus.OK, "Cached - Services assignment summary", costSummary);

    }

    @ApiOperation(value = "Delete a service assignment to a device from system", notes = "Deletes a service assignment to a device in the system, shows an error message when the service assignment doesn't exist.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted service assignment to a device"),
            @ApiResponse(code = 400, message = "Service assignment to a device ID doesn't exist")
    })
    @DeleteMapping(value = "/{id}")
    private ResponseObject<String> deleteById(
            @ApiParam(value = "Unique identifier of the service assignment to a device")
            @PathVariable String id) {
        if (serviceAssignService.getServiceAssignmentById(id).isPresent()) {
            serviceAssignService.deleteById(id);
            return new ResponseObject<>(HttpStatus.OK, "Service assignment to Device deleted succesfully", null);
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Service assignment ID " + id + " doesn't exists", null);
        }
    }
}
