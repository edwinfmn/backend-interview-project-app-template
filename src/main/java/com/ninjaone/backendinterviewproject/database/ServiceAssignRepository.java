package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.ServiceAssign;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceAssignRepository extends CrudRepository<ServiceAssign, String> {}
