package pt.ul.fc.css.soccernow.model;

public enum ChampionshipStatus {
  PLANNED("Planned"),
  ONGOING("Ongoing"),
  COMPLETED("Completed"),
  CANCELLED("Cancelled");

  private final String displayName;

  ChampionshipStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
