package pt.ul.fc.css.soccernow.model;

public enum UserType {
  PLAYER("Player"),
  REFEREE("Referee");

  private final String displayName;

  UserType(String displayName) {
    this.displayName = displayName;
  }

  public String getType() {
    return displayName;
  }
}
