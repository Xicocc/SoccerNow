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
}
