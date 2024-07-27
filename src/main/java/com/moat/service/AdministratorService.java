package com.moat.service;

import com.moat.entity.Administrator;
import com.moat.exception.MOATValidationException;

import javax.persistence.NoResultException;
import java.util.List;

public interface AdministratorService {
  public List<Administrator> selectAll() throws NoResultException;

  public Administrator selectById(int id) throws NoResultException;

  public Administrator selectByUsername(String username)
      throws NoResultException;

  void createAdministrator(Administrator administrator)
      throws MOATValidationException;
}
