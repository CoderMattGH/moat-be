package com.moat.dao;

import com.moat.entity.MOATUser;

import javax.persistence.NoResultException;
import java.util.List;

public interface UserDao {
  void saveOrUpdate(MOATUser user);

  List<MOATUser> selectAll();

  MOATUser selectByEmail(String email) throws NoResultException;

  MOATUser selectById(Long id) throws NoResultException;

  MOATUser selectByUsername(String username) throws NoResultException;
}
