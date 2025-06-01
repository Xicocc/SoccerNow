package pt.ul.fc.css.soccernow.model;

public enum GameStatus {
  SCHEDULED("Scheduled"),
  COMPLETED("Completed"),
  CANCELLED("Cancelled");

  private final String displayName;

  GameStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getType() {
    return displayName;
  }
}
