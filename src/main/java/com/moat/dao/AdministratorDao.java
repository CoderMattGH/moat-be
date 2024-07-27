package com.moat.dao;

import com.moat.entity.Administrator;

import javax.persistence.NoResultException;
import java.util.List;

public interface AdministratorDao {
  List<Administrator> selectAll();

  Administrator selectById(int id) throws NoResultException;

  Administrator selectByUsername(String username);

  void save(Administrator administrator);

  void delete(Administrator administrator);
}
