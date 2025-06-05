package pt.ul.fc.css.soccernow.exceptions;

public class RefereeNotFoundException extends RuntimeException {
  public RefereeNotFoundException(Long id) {
    super("Referee not found with id: " + id);
  }

  public RefereeNotFoundException(String name) {
    super("Referee not found with name: " + name);
  }
}
