package com.moat.constant;

import com.moat.entity.MOATUser;

public class ValidationMsg {
  public final static String INCORRECT_PATH_VAR_DATA_TYPE =
      "Path variable was the incorrect data type!";
  public final static String INCORRECT_DATA_TYPE =
      "One or more fields was the incorrect data type!";
  public final static String ERROR_PROCESSING_REQUEST =
      "Error processing request!";
  public final static String JSON_PARSE_ERROR = "Error parsing JSON object!";
  public final static String UNKNOWN_SERVER_ERROR =
      "An unknown server error occurred!";

  public final static String ERROR_GETTING_ENDPOINTS =
      "Unable to get endpoints!";

  public final static String USER_ALREADY_EXISTS = "User already exists!";
  public final static String USER_DOES_NOT_EXIST = "User does not exist!";
  public final static String USERS_NOT_FOUND = "No users found!";
  public final static String ERROR_GETTING_USERS = "Unable to get users!";
  public final static String ERROR_GETTING_USER = "Unable to get user!";
  public final static String ERROR_POSTING_USER = "Unable to create user!";

  public final static String EMAIL_ALREADY_EXISTS =
      "Email address already exists!";

  public final static String SCORES_NOT_FOUND = "No scores found!";
  public final static String SCORE_DOES_NOT_EXIST = "Score does not exist!";
  public final static String ERROR_DELETING_SCORES = "Unable to delete scores!";
  public final static String ERROR_GETTING_SCORES = "Unable to get scores!";
  public final static String ERROR_GETTING_SCORE = "Unable to get score!";
  public final static String ERROR_POSTING_SCORE = "Unable to save score!";

  public final static String USERNAME_NULL_MSG = "Username cannot be null!";
  public final static String USERNAME_LENGTH_MSG =
      String.format("Username must be between %d and %d characters in length!",
          MOATUser.USERNAME_MIN_LENGTH, MOATUser.USERNAME_MAX_LENGTH);
  public final static String USERNAME_PATTERN_MSG =
      "Username must only contain uppercase alpha-numeric characters!";

  public final static String EMAIL_NULL_MSG = "Email cannot be null!";
  public final static String EMAIL_LENGTH_MSG =
      String.format("Email must be between %d and %d characters in length!",
          MOATUser.EMAIL_MIN_LENGTH, MOATUser.EMAIL_MAX_LENGTH);
  public final static String EMAIL_PATTERN_MSG =
      "Email is not a valid email address!";

  public final static String PASSWORD_NULL_MSG = "Password cannot be null!";
  public final static String PASSWORD_LENGTH_MSG =
      String.format("Password must be between %d and %d characters in length!",
          MOATUser.PASSWORD_MIN_LENGTH, MOATUser.PASSWORD_MAX_LENGTH);
  public final static String PASSWORD_PATTERN_MSG =
      "Password contains invalid symbols!";

  public final static String ID_NULL_MSG = "Id cannot be null!";
  public final static String ID_VALUE_MSG = "Id must be bigger than 0!";

  private ValidationMsg() {}
}
