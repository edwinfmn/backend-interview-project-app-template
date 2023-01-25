package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.model.Serv;
import com.ninjaone.backendinterviewproject.response.ResponseObject;
import com.ninjaone.backendinterviewproject.service.ServService;
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
@WebMvcTest(ServController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class ServiceControllerTest {
    public static final String ID = "3500";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ServService service;

    private Serv servEntity;
    private ResponseObject<Serv> responseObjectCreate;
    private ResponseObject<Serv> responseObjectService;
    private ResponseObject<Serv> responseObjectDelete;

    @BeforeEach
    void setup(){

        servEntity = new Serv(ID, "Office","MAC", new BigDecimal(10));
        responseObjectCreate = new ResponseObject<>(HttpStatus.CREATED, "Service created successfully", servEntity);
        responseObjectService = new ResponseObject<>(HttpStatus.OK, null, servEntity);
        responseObjectDelete = new ResponseObject<>(HttpStatus.BAD_REQUEST, "Service ID 3500 doesn't exists", null);
    }

    @Test
    void postServiceData() throws Exception {
        when(service.saveService(any())).thenReturn(servEntity);

        String serviceEntityString = objectMapper.writeValueAsString(servEntity);

        mockMvc.perform(post("/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceEntityString))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectCreate)));
    }

    @Test
    void getServiceData() throws Exception {
        when(service.getServiceById(ID)).thenReturn(Optional.of(servEntity));

        mockMvc.perform(get("/service/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectService)));
    }

    @Test
    void getAllServicesData() throws Exception {
        when(service.listAllServices()).thenReturn(List.of(servEntity));

        String listOfServicesString = objectMapper.writeValueAsString(new ArrayList<Serv>().add(servEntity));

        mockMvc.perform(get("/service/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteServiceData() throws Exception {
        doNothing().when(service).deleteById(ID);

        mockMvc.perform(delete("/service/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(responseObjectDelete)));
    }
}
