package pt.ul.fc.css.soccernow.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.soccernow.model.Referee;

@Repository
public interface RefereeRepository extends UserRepository {

  @Query("SELECT r FROM Referee r " + "ORDER BY r.gamesParticipated DESC " + "LIMIT 1")
  Referee findTopRefereeByGamesParticipated();
}
