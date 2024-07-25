package com.moat.service;

import com.moat.entity.Administrator;

import java.util.List;

public interface AdministratorService {
  /**
   * Returns the Administrator assigned to the ID.
   *
   * @param id An integer representing the ID of the Administrator.
   * @return Returns null when no Administrator is found.
   */
  Administrator findById(int id);

  /**
   * Returns the Administrator assigned to the username.
   *
   * @param username The string representing the Username of the Administrator.
   * @return Returns null when no Administrator is found.
   */
  Administrator findByUsername(String username);

  /**
   * Persists or merges the Administrator to the database.
   *
   * @param administrator An Administrator object to persist.
   */
  void saveOrUpdate(Administrator administrator);

  /**
   * Removes the selected Administrator from the database.
   *
   * @param administrator An Administrator object to remove.
   */
  void delete(Administrator administrator);

  /**
   * Returns a List of all Administrator objects.
   */
  List<Administrator> findAll();
}
