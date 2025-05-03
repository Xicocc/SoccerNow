package pt.ul.fc.css.soccernow.service;

import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.RefereeRegistrationDTO;
import pt.ul.fc.css.soccernow.model.Referee;
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
    return refereeRepository.save(referee);
  }

  public Referee getTopReferee() {
    return refereeRepository.findTopRefereeByGamesParticipated();
  }
}
