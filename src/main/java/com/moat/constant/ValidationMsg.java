package com.moat.constant;

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

  public final static String INVALID_CREDENTIALS = "Invalid credentials!";
  public final static String DISABLED_EXCEPTION =
      "User account has been disabled!";
  public final static String ERROR_LOGGING_IN = "Unable to login!";
  public final static String ERROR_UNAUTHORISED =
      "You are not authorised to perform this action!";

  public final static String JWT_NO_AUTH_HEADER =
      "No authorization header set!";
  public final static String JWT_PARSE_ERROR = "Unable to parse JWT token!";

  public final static String USERNAME_ALREADY_EXISTS =
      "Username already exists!";
  public final static String USER_ALREADY_EXISTS = "User already exists!";
  public final static String USER_DOES_NOT_EXIST = "User does not exist!";
  public final static String USERS_NOT_FOUND = "No users found!";
  public final static String ERROR_GETTING_USERS = "Unable to get users!";
  public final static String ERROR_GETTING_USER = "Unable to get user!";
  public final static String ERROR_POSTING_USER = "Unable to create user!";
  public final static String ERROR_UPDATING_USER = "Unable to update user!";

  public final static String ADMIN_ALREADY_EXISTS = "Admin already exists!";
  public final static String ADMIN_DOES_NOT_EXIST = "Admin does not exist!";
  public final static String ADMINS_NOT_FOUND = "No admins found!";
  public final static String ERROR_GETTING_ADMIN = "Unable to get admin!";
  public final static String ERROR_POSTING_ADMIN = "Unable to create admin!";

  public final static String EMAIL_ALREADY_EXISTS =
      "Email address already exists!";

  public final static String SCORES_NOT_FOUND = "No scores found!";
  public final static String SCORE_DOES_NOT_EXIST = "Score does not exist!";
  public final static String ERROR_DELETING_SCORES = "Unable to delete scores!";
  public final static String ERROR_GETTING_SCORES = "Unable to get scores!";
  public final static String ERROR_GETTING_SCORE = "Unable to get score!";
  public final static String ERROR_POSTING_SCORE = "Unable to save score!";

  public final static String USERNAME_NULL_MSG = "'username' cannot be null!";
  public final static String USERNAME_LENGTH_MSG = String.format(
      "'username' must be between %d and %d characters in length!",
      Constants.USERNAME_MIN_LENGTH, Constants.USERNAME_MAX_LENGTH);
  public final static String USERNAME_PATTERN_MSG =
      "'username' must only contain uppercase alpha-numeric characters!";

  public final static String EMAIL_NULL_MSG = "'email' cannot be null!";
  public final static String EMAIL_LENGTH_MSG =
      String.format("'email' must be between %d and %d characters in length!",
          Constants.EMAIL_MIN_LENGTH, Constants.EMAIL_MAX_LENGTH);
  public final static String EMAIL_PATTERN_MSG =
      "'email' is not a valid email address!";

  public final static String PASSWORD_NULL_MSG = "'password' cannot be null!";
  public final static String PASSWORD_LENGTH_MSG = String.format(
      "'password' must be between %d and %d characters in length!",
      Constants.PASSWORD_MIN_LENGTH, Constants.PASSWORD_MAX_LENGTH);
  public final static String PASSWORD_PATTERN_MSG =
      "'password' contains invalid symbols!";

  public final static String SCORE_NULL_MSG = "'score' cannot be null!";
  public final static String SCORE_POSITIVE_INT_MSG =
      "'score' must be a positive integer!";

  public final static String USER_ID_NULL_MSG = "'userId' cannot be null!";
  public final static String USER_ID_VALUE_MSG =
      "'userId' must be bigger than 0!";

  public final static String ID_NULL_MSG = "'id' cannot be null!";
  public final static String ID_VALUE_MSG = "'id' must be bigger than 0!";

  public final static String HITS_NULL_MSG = "'hits' cannot be null!";
  public final static String HITS_VALUE_MSG = "'hits' cannot be negative!";

  public final static String NO_HITS_NULL_MSG = "'nohits' cannot be null!";
  public final static String NO_HITS_VALUE_MSG = "'nohits' cannot be negative!";

  public final static String MISSES_NULL_MSG = "'misses' cannot be null!";
  public final static String MISSES_VALUE_MSG = "'misses' cannot be negative!";

  private ValidationMsg() {}
}
