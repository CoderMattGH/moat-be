package com.moat.dao;

import com.moat.entity.MOATAdmin;

import javax.persistence.NoResultException;
import java.util.List;

public interface AdminDao {
  List<MOATAdmin> selectAll();

  MOATAdmin selectById(int id) throws NoResultException;

  MOATAdmin selectByUsername(String username) throws NoResultException;

  void saveOrUpdate(MOATAdmin admin);

  int deleteById(Long id);
}
