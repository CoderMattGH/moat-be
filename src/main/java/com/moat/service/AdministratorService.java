package com.moat.service;

import com.moat.entity.Administrator;

import java.util.List;

public interface AdministratorService {
    Administrator findById(int id);
    Administrator findByUsername(String username);
    void saveOrUpdate(Administrator administrator);
    void delete(Administrator administrator);
    List<Administrator> findAll();
}
