package com.moat.constant;

public class ValidationMsg {
  private ValidationMsg() {
  }

  public final static String INCORRECT_DATA_TYPE =
      "One or more fields was the incorrect data type!";
  public final static String JSON_PARSE_ERROR = "Error parsing JSON object!";

  public final static String USER_DOES_EXIST = "User does not exist!";
}
