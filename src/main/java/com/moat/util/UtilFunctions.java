package com.moat.util;

public class UtilFunctions {
  private UtilFunctions() {}

  public static double roundToTwoDecimalPlaces(double number) {
    return (double) Math.round(number * 100) / 100;
  }

  public static double getAveragePercentage(double dividend, double divisor,
      boolean rounded) {
    double avgAccuracy;

    if (dividend == 0) {
      avgAccuracy = 0.0;
    } else {
      avgAccuracy = (dividend / divisor) * 100;
    }

    if (rounded) {
      avgAccuracy = roundToTwoDecimalPlaces(avgAccuracy);
    }

    return avgAccuracy;
  }
}
