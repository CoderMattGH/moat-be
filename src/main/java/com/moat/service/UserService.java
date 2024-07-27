package com.moat.service;

import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import com.moat.exception.MOATValidationException;

import javax.persistence.NoResultException;
import java.util.List;

public interface UserService {
  List<MOATUser> selectAllUsers();

  MOATUser selectByUsername(String username) throws NoResultException;

  public void createUser(MOATUser user)
      throws AlreadyExistsException, MOATValidationException;
}
