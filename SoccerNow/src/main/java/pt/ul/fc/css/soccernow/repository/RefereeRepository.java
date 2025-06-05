package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Referee;

@Repository
public interface RefereeRepository extends UserRepository {

  @Query("SELECT r FROM Referee r")
  List<Referee> findAllReferees();

  @Query("SELECT r FROM Referee r " + "ORDER BY r.gamesParticipated DESC " + "LIMIT 1")
  Referee findTopRefereeByGamesParticipated();

  @Query("SELECT r FROM Referee r WHERE r.name = :name")
  Referee findByName(String name);

  @Query("SELECT r FROM Referee r WHERE r.gamesParticipated = :games")
  List<Referee> findByGamesParticipated(int games);

  @Query("SELECT r FROM Referee r WHERE r.cardsShown = :cards")
  List<Referee> findByCardsShown(int cards);
}
