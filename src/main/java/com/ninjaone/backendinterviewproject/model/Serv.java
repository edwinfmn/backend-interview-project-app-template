package com.ninjaone.backendinterviewproject.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "Service")
@Getter
@Setter
@ApiModel(description = "This is a instance or representation of a Service")
public class Serv {

    @ApiModelProperty(notes = "Unique Identifier of the service", required = true)
    @Column(nullable = false)
    @Id
    private String id;

    @ApiModelProperty(notes = "Name for the service, Ex: Antivirus, Backup, Office, etc.", required = true)
    @Column(name = "name", nullable = false)
    private String serviceName;

    @ApiModelProperty(notes = "Type of the service that must match the device System Name, Ex: MAC, WINDOWS, LINUX, etc.", required = true)
    @Column(name = "type", nullable = false)
    private String type;

    @ApiModelProperty(notes = "Price defined for the service")
    private BigDecimal price;

    public Serv(){}

    public Serv(String id, String serviceName, String type, BigDecimal price) {
        this.id = id;
        this.serviceName = serviceName;
        this.type = type;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if( this.id.equals(((Serv) o).getId()) )
            return true;

        if( this.serviceName.trim().equalsIgnoreCase(((Serv)o).getServiceName().trim())
            && this.type.trim().equalsIgnoreCase(((Serv)o).getType().trim()) )
            return true;
        else
            return false;
    }
}
