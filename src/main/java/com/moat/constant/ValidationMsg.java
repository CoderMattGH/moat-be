package com.moat.constant;

public class ValidationMsg {
  public final static String INCORRECT_PATH_VAR_DATA_TYPE =
      "Path variable was the incorrect data type!";
  public final static String INCORRECT_DATA_TYPE =
      "One or more fields was the incorrect data type!";
  public final static String JSON_PARSE_ERROR = "Error parsing JSON object!";

  public final static String USER_DOES_NOT_EXIST = "User does not exist!";

  public final static String SCORES_NOT_FOUND = "No scores found!";
  public final static String SCORE_DOES_NOT_EXIST = "Score does not exist!";
  public final static String ERROR_DELETING_SCORES = "Unable to delete scores!";
  public final static String ERROR_GETTING_SCORES = "Unable to get scores!";
  public final static String ERROR_POSTING_SCORE = "Unable to save score!";

  private ValidationMsg() {}
}
