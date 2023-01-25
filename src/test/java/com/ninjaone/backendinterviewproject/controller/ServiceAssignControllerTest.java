package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.Serv;
import com.ninjaone.backendinterviewproject.model.ServiceAssign;
import com.ninjaone.backendinterviewproject.response.ResponseObject;
import com.ninjaone.backendinterviewproject.service.CostCalculatorService;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import com.ninjaone.backendinterviewproject.service.ServService;
import com.ninjaone.backendinterviewproject.service.ServiceAssignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class})
@WebMvcTest(ServiceAssignController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class ServiceAssignControllerTest {
    public static final String ID = "4500";
    public static final String DEVICE_ID = "100";
    public static final String SERVCICE_ID = "500";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;
    @MockBean
    private ServService servService;
    @MockBean
    private ServiceAssignService serviceAssignService;
    @MockBean
    private CostCalculatorService costCalculatorService;

    private Device deviceEntity;
    private Serv servEntity;
    private ServiceAssign serviceAssignEntity;
    private ResponseObject<ServiceAssign> responseObjectCreate;
    private ResponseObject<ServiceAssign> responseObjectDelete;

    @BeforeEach
    void setup(){

        deviceEntity = new Device(DEVICE_ID, "WINDOWS", "Windows Server");
        servEntity = new Serv(SERVCICE_ID, "Office","WINDOWS", new BigDecimal(10));
        serviceAssignEntity = new ServiceAssign(ID, deviceEntity, servEntity);
        responseObjectCreate = new ResponseObject<>(HttpStatus.BAD_REQUEST, "ERROR - Device ID 100 doesn't exist", null);
        responseObjectDelete = new ResponseObject<>(HttpStatus.BAD_REQUEST, "Service assignment ID 4500 doesn't exists", null);
    }

    @Test
    void postServiceAssignData() throws Exception {
        when(serviceAssignService.saveServiceByDevice(any())).thenReturn(serviceAssignEntity);

        String assignEntityString = objectMapper.writeValueAsString(serviceAssignEntity);

        mockMvc.perform(post("/servicebydevice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(assignEntityString))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectCreate)));
    }

    @Test
    void getServiceAssignData() throws Exception {
        when(serviceAssignService.getServiceAssignmentById(ID)).thenReturn(Optional.of(serviceAssignEntity));

        mockMvc.perform(get("/servicebydevice/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(serviceAssignEntity)));
    }

    @Test
    void getAllServiceAssignsData() throws Exception {
        when(serviceAssignService.listAllServicesAssignments()).thenReturn(List.of(serviceAssignEntity));

        String listOfDevicesString = objectMapper.writeValueAsString(new ArrayList<ServiceAssign>().add(serviceAssignEntity));

        mockMvc.perform(get("/servicebydevice/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteServiceAssignData() throws Exception {
        doNothing().when(serviceAssignService).deleteById(ID);

        mockMvc.perform(delete("/servicebydevice/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectDelete)));
    }
}

