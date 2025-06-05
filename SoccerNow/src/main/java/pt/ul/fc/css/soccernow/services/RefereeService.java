package pt.ul.fc.css.soccernow.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.RefereeRegistrationDTO;
import pt.ul.fc.css.soccernow.exceptions.RefereeNotFoundException;
import pt.ul.fc.css.soccernow.model.Referee;
import pt.ul.fc.css.soccernow.model.User;
import pt.ul.fc.css.soccernow.model.UserType;
import pt.ul.fc.css.soccernow.repository.RefereeRepository;

@Service
public class RefereeService {
  private final RefereeRepository refereeRepository;
  private final UserService userService;

  public RefereeService(RefereeRepository refereeRepository, UserService userService) {
    this.refereeRepository = refereeRepository;
    this.userService = userService;
  }

  public Referee registerReferee(RefereeRegistrationDTO dto) {
    if (userService.existsByName(dto.getName())) {
      throw new IllegalArgumentException("Referee name already exists");
    }

    Referee referee = new Referee();
    referee.setName(dto.getName());
    referee.setAge(dto.getAge());
    referee.setUserType(UserType.REFEREE);
    return refereeRepository.save(referee);
  }

  public List<Referee> getAllReferees() {
    return refereeRepository.findAllReferees();
  }

  public Referee getTopReferee() {
    return refereeRepository.findTopRefereeByGamesParticipated();
  }

  public Referee addGamesParticipated(Long refereeId, int games) {
    Referee referee = getRefereeById(refereeId);

    if (games < 0) {
      throw new IllegalArgumentException("Games cannot be negative");
    }

    referee.setGamesParticipated(referee.getGamesParticipated() + games);
    return refereeRepository.save(referee);
  }

  public Referee addCardsShown(Long refereeId, int cards) {
    Referee referee = getRefereeById(refereeId);

    if (cards < 0) {
      throw new IllegalArgumentException("Cards cannot be negative");
    }

    referee.setCardsShown(referee.getCardsShown() + cards);
    return refereeRepository.save(referee);
  }

  public Referee getRefereeById(Long id) {
    User user = refereeRepository.findById(id).orElseThrow(() -> new RefereeNotFoundException(id));
    if (!(user instanceof Referee)) {
      throw new IllegalArgumentException("User with ID " + id + " is not a referee.");
    }
    return (Referee) user;
  }

  public Referee getRefereeByName(String name) {
    Referee referee = refereeRepository.findByName(name);
    if (referee == null) {
      throw new RefereeNotFoundException(name);
    }
    return referee;
  }

  public List<Referee> getRefereesByGamesParticipated(int games) {
    return refereeRepository.findByGamesParticipated(games);
  }

  public List<Referee> getRefereesByCardsShown(int cards) {
    return refereeRepository.findByCardsShown(cards);
  }

  public List<Referee> filterReferees(String name, Integer minGames, Integer minCards) {
    return refereeRepository.findAll().stream()
        .filter(user -> user instanceof Referee)
        .map(user -> (Referee) user)
        .filter(r -> name == null || r.getName().toLowerCase().contains(name.toLowerCase()))
        .filter(r -> minGames == null || r.getGamesParticipated() >= minGames)
        .filter(r -> minCards == null || r.getCardsShown() >= minCards)
        .collect(Collectors.toList());
  }
}
