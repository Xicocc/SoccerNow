package pt.ul.fc.css.soccernow.exceptions;

public class TeamNotFoundException extends RuntimeException {
  public TeamNotFoundException(Long id) {
    super("Team not found with id: " + id);
  }
}
