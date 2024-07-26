package com.moat.service;

import com.moat.entity.MOATUser;

import java.util.List;

public interface UserService {
  List<MOATUser> selectAllUsers();
}
