package com.moat.service;

import com.moat.dto.AdminDTO;
import com.moat.entity.MOATAdmin;
import com.moat.exception.AlreadyExistsException;

import javax.persistence.NoResultException;
import java.util.List;

public interface AdminService {
  List<AdminDTO> selectAll() throws NoResultException;

  AdminDTO selectById(Long id) throws NoResultException;

  AdminDTO selectByUsername(String username) throws NoResultException;

  AdminDTO create(MOATAdmin admin) throws AlreadyExistsException;

  AdminDTO create(AdminDTO admin) throws AlreadyExistsException;

  AdminDTO marshallIntoDTO(MOATAdmin admin);

  public List<AdminDTO> marshallIntoDTO(List<MOATAdmin> admins);
}
