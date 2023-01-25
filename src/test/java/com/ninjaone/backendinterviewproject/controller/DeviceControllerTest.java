package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.response.ResponseObject;
import com.ninjaone.backendinterviewproject.service.DeviceService;
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
@WebMvcTest(DeviceController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class DeviceControllerTest {
    public static final String ID = "2500";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    private Device deviceEntity;
    private ResponseObject<Device> responseObjectCreate;
    private ResponseObject<Device> responseObjectDevice;
    private ResponseObject<Device> responseObjectDelete;

    @BeforeEach
    void setup(){

        deviceEntity = new Device(ID, "WINDOWS", "Windows Server");
        responseObjectCreate = new ResponseObject<>(HttpStatus.CREATED, "Device created successfully", deviceEntity);
        responseObjectDevice = new ResponseObject<>(HttpStatus.OK, null, deviceEntity);
        responseObjectDelete = new ResponseObject<>(HttpStatus.BAD_REQUEST, "Device ID 2500 doesn't exists", null);
    }

    @Test
    void postDeviceData() throws Exception {
        when(deviceService.saveDevice(any())).thenReturn(deviceEntity);

        String deviceEntityString = objectMapper.writeValueAsString(deviceEntity);

        mockMvc.perform(post("/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deviceEntityString))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectCreate)));
    }

    @Test
    void getDeviceData() throws Exception {
        when(deviceService.getDeviceById(ID)).thenReturn(Optional.of(deviceEntity));

        mockMvc.perform(get("/device/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectDevice)));
    }

    @Test
    void getAllDevicesData() throws Exception {
        when(deviceService.listAllDevices()).thenReturn(List.of(deviceEntity));

        String listOfDevicesString = objectMapper.writeValueAsString(new ArrayList<Device>().add(deviceEntity));

        mockMvc.perform(get("/device/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteDeviceData() throws Exception {
        doNothing().when(deviceService).deleteById(ID);

        mockMvc.perform(delete("/device/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectDelete)));
    }
}
