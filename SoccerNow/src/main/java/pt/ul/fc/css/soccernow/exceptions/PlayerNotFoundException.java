package pt.ul.fc.css.soccernow.exceptions;

public class PlayerNotFoundException extends RuntimeException {
  public PlayerNotFoundException(Long id) {
    super("Player not found with id: " + id);
  }

  public PlayerNotFoundException(String name) {
    super("Player not found with name: " + name);
  }
}
