package com.ninjaone.backendinterviewproject.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ApiModel(description = "This is a instance or representation of a Device")
public class Device {

    @ApiModelProperty(notes = "Unique Identifier of the device", required = true)
    @Column(nullable = false)
    @Id
    private String id;

    @ApiModelProperty(notes = "System Name for the device, Ex: MAC, WINDOWS, LINUX, etc.", required = true)
    @Column(name = "system_name", nullable = false)
    private String systemName;

    @ApiModelProperty(notes = "Type to describe the device, Ex: Windows Server, Mac Book Pro, Windows Enterprise, Arch Linux, etc.")
    private String type;

    public Device(){}

    public Device(String id, String systemName, String type) {
        this.id = id;
        this.systemName = systemName;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if(this.id.equals(((Device)o).getId()))
            return true;

        return this.systemName.trim().equalsIgnoreCase(((Device) o).getSystemName().trim())
                && this.type.trim().equalsIgnoreCase(((Device) o).getType().trim());
    }
}
