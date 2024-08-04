package com.moat.service;

import com.moat.dto.UserDTO;
import com.moat.entity.MOATUser;
import com.moat.exception.AlreadyExistsException;
import com.moat.exception.NotFoundException;

import java.util.List;

public interface UserService {
  UserDTO create(MOATUser user) throws AlreadyExistsException;

  UserDTO create(UserDTO user) throws AlreadyExistsException;

  UserDTO updateUserDetails(UserDTO user)
      throws AlreadyExistsException, NotFoundException;

  UserDTO updateUserPassword(UserDTO user) throws NotFoundException;

  List<UserDTO> selectAll() throws NotFoundException;

  UserDTO selectById(Long id) throws NotFoundException;

  UserDTO selectByUsername(String username) throws NotFoundException;

  UserDTO marshallIntoDTO(MOATUser user);

  List<UserDTO> marshallIntoDTO(List<MOATUser> users);
}
