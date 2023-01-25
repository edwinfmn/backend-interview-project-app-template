package com.ninjaone.backendinterviewproject.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ApiModel(description = "This is a representation of a Service Assignment to a Device")
public class ServiceAssign {

    @ApiModelProperty(notes = "Unique Identifier of the assignment", required = true)
    @Column(nullable = false)
    @Id
    private String id;

    @ApiModelProperty(notes = "Device information", required = true)
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @ApiModelProperty(notes = "Service information", required = true)
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Serv service;

    public ServiceAssign(){}

    public ServiceAssign(String id, Device device, Serv service) {
        this.id = id;
        this.device = device;
        this.service = service;
    }

}
