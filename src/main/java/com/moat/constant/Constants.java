package com.moat.constant;

public class Constants {
  public final static String FILTERS_DIR = "filters";
  public final static String FILTER_PROF_FILE = "prof_filter.txt";
  public final static String ENDPOINTS_FILE_PATH = "endpoints.json";

  public final static int PASSWORD_MIN_LENGTH = 5;
  public final static int PASSWORD_MAX_LENGTH = 15;
  public final static String PASSWORD_PATTERN =
      "^[*.!@#$%^&(){}\\[\\]:;,.,.?/~_+-=|A-Za-z0-9]+$";

  public final static int EMAIL_MIN_LENGTH = 4;
  public final static int EMAIL_MAX_LENGTH = 30;
  public final static String EMAIL_PATTERN =
      "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

  public final static int USERNAME_MIN_LENGTH = 4;
  public final static int USERNAME_MAX_LENGTH = 8;
  public final static String USERNAME_PATTERN = "^[A-Z0-9]+$";

  private Constants() {}
}
