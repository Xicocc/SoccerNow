package pt.ul.fc.css.soccernow.model;

public enum Position {
  GOALKEEPER("Goalkeeper"),
  SWEEPER("Sweeper/Defender"),
  RIGHT_WINGER("Right Winger"),
  LEFT_WINGER("Left winger"),
  FORWARD("Forward"),
  UNKNOWN("Not specified");

  private final String displayName;

  Position(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}
