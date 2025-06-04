package com.soccernow.fx.util;

import javafx.stage.Stage;

public class AppWindowManager {
  public static double width = 602;
  public static double height = 430;

  public static void persistSize(Stage stage) {
    width = Math.max(stage.getWidth(), 602);
    height = Math.max(stage.getHeight(), 430);
  }

  public static void applySize(Stage stage) {
    stage.setWidth(width);
    stage.setHeight(height);
    stage.setMinWidth(602);
    stage.setMinHeight(430);
  }
}
