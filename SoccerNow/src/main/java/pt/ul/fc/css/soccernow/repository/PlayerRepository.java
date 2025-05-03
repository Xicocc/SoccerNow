package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Position;

@Repository
public interface PlayerRepository extends UserRepository {

  @Query("SELECT p FROM Player p")
  List<Player> findAllPlayers();

  @Query("SELECT p FROM Player p WHERE p.preferredPosition = :position")
  List<Player> findByPosition(Position position);

  @Query(
      "SELECT AVG(p.goalsScored * 1.0 / p.gamesPlayed) "
          + "FROM Player p "
          + "WHERE p.name = :name AND p.gamesPlayed > 0")
  Double findAverageGoalsPerGameByName(String name);

  @Query("SELECT p FROM Player p ORDER BY p.redCards DESC")
  List<Player> findPlayersByMostRedCards();

  @Query("SELECT p FROM Player p ORDER BY p.redCards DESC LIMIT :limit")
  List<Player> findTopPlayersByRedCards(int limit);
}
