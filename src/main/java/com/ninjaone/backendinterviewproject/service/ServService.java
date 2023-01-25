package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.ServRepository;
import com.ninjaone.backendinterviewproject.model.Serv;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServService {

    private final ServRepository servRepository;

    public ServService(ServRepository servRepository) {
        this.servRepository = servRepository;
    }

    public Serv saveService(Serv serv){
        return servRepository.save(serv);
    }

    public List<Serv> listAllServices() { return (List<Serv>) servRepository.findAll(); }

    public Optional<Serv> getServiceById(String id){
        return servRepository.findById(id);
    }

    public void deleteById(String id) {
        servRepository.deleteById(id);
    }

    public List<Serv> searchByName(String name) {
        return servRepository.searchByName(name);
    }
}
