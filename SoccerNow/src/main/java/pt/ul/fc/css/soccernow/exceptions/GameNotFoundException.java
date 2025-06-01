package pt.ul.fc.css.soccernow.exceptions;

public class GameNotFoundException extends RuntimeException {
  public GameNotFoundException(Long id) {
    super("Game not found with id: " + id);
  }

  public GameNotFoundException(String name) {
    super("Game not found with name: " + name);
  }
}
