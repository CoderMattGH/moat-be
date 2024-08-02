package com.moat.service;

import com.moat.dto.UserDTO;
import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;

import javax.persistence.NoResultException;
import java.util.List;

public interface UserService {
  UserDTO create(MOATUser user) throws AlreadyExistsException;

  UserDTO create(UserDTO user) throws AlreadyExistsException;

  UserDTO updateUserDetails(UserDTO user)
      throws AlreadyExistsException, NoResultException;

  List<UserDTO> selectAll() throws NoResultException;

  UserDTO selectById(Long id) throws NoResultException;

  UserDTO selectByUsername(String username) throws NoResultException;

  UserDTO marshallIntoDTO(MOATUser user);

  List<UserDTO> marshallIntoDTO(List<MOATUser> users);
}
