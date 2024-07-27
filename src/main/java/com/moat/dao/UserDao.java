package com.moat.dao;

import com.moat.entity.MOATUser;

import javax.persistence.NoResultException;
import java.util.List;

public interface UserDao {
  List<MOATUser> selectAllUsers();

  MOATUser selectUserByUsername(String username) throws NoResultException;

  void saveUser(MOATUser user);
}
