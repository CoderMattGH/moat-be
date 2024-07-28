package com.moat.service;

import com.moat.entity.Administrator;
import com.moat.exception.MOATValidationException;

import javax.persistence.NoResultException;
import java.util.List;

public interface AdministratorService {
  List<Administrator> selectAll() throws NoResultException;

  Administrator selectById(int id) throws NoResultException;

  Administrator selectByUsername(String username) throws NoResultException;

  void createAdministrator(Administrator administrator)
      throws MOATValidationException;
}
