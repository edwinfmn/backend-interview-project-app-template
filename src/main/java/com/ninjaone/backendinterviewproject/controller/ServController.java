package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.model.Serv;
import com.ninjaone.backendinterviewproject.response.ResponseObject;
import com.ninjaone.backendinterviewproject.service.ServService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "Service Controller", value = "Service Controller")
@RequestMapping("/service")
public class ServController {

    private final ServService servService;

    public ServController(ServService servService) {
        this.servService = servService;
    }

    @ApiOperation(value = "Get a list of all Service", notes = "Get a list of all services registered on database.")
    @GetMapping("/all")
    private List<Serv> getAllServices() {
        return servService.listAllServices();
    }

    @ApiOperation(value = "Find a service by Id", notes = "Get a service by the unique identifier, shows a message when the service doesn't exist.")
    @GetMapping("/{id}")
    private ResponseObject<Serv> getServiceById(
            @ApiParam(value = "Unique identifier of the service to retrieve")
            @PathVariable String id) {
        try {
            return new ResponseObject<>(HttpStatus.OK, null, servService.getServiceById(id).orElseThrow());
        } catch (Exception e) {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Service ID -" + id + "- doesn't exists", null);
        }
    }

    @ApiOperation(value = "Find all services that match the name given", notes = "Get a list of services as result of a search by Name, it shows an empty list when no services are found.")
    @GetMapping("/name/{name}")
    private List<Serv> searchByName(
            @ApiParam(value = "Service Name")
            @PathVariable String name) {
        return servService.searchByName(name);
    }

    @ApiOperation(value = "Create a new service", notes = "Creates a new service in the system, shows an error message when the service already exists, duplicated services are not allowed.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created service"),
            @ApiResponse(code = 400, message = "Service data repeated or ID already exists")
    })
    @PostMapping
    private ResponseObject<Serv> saveService(
            @ApiParam(value = "Service information for register a new service")
            @RequestBody Serv service) {
        boolean valid = servService.getServiceById(service.getId()).isEmpty();

        service.setServiceName(service.getServiceName().toUpperCase().trim());
        service.setType(service.getType().trim());

        if(servService.searchByName(service.getServiceName()).stream().anyMatch(s -> s.equals(service)))
            valid = false;

        if(valid)
            return new ResponseObject<>(HttpStatus.CREATED, "Service created successfully", servService.saveService(service));
        else
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "ERROR - Service already exists", null);
    }

    @ApiOperation(value = "Delete a service from system", notes = "Deletes a service in the system, shows an error message when the service doesn't exist.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted service"),
            @ApiResponse(code = 400, message = "Service ID doesn't exist")
    })
    @DeleteMapping(value = "/{id}")
    private ResponseObject<String> deleteById(
            @ApiParam(value = "Unique identifier of the service")
            @PathVariable String id) {
        if (servService.getServiceById(id).isPresent()) {
            servService.deleteById(id);
            return new ResponseObject<>(HttpStatus.OK, "Service deleted succesfully", null);
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Service ID " + id + " doesn't exists", null);
        }
    }
}
