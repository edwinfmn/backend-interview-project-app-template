package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends CrudRepository<Device, String> {

    @Query("SELECT d FROM Device d WHERE UPPER(d.systemName) = UPPER(:name)")
    List<Device> searchByName(@Param("name") String name);

}
