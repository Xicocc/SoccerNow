package pt.ul.fc.css.soccernow.exceptions;

public class ChampionshipNotFoundException extends RuntimeException {
  public ChampionshipNotFoundException(Long id) {
    super("Championship not found with id: " + id);
  }

  public ChampionshipNotFoundException(String name) {
    super("Championship not found with name: " + name);
  }
}
