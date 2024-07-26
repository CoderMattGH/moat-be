package com.moat.service;

import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;

import javax.persistence.NoResultException;
import java.util.List;

public interface UserService {
  List<MOATUser> selectAllUsers();

  MOATUser selectUserByUsername(String username) throws NoResultException;

  MOATUser createUser(MOATUser user) throws AlreadyExistsException;
}
