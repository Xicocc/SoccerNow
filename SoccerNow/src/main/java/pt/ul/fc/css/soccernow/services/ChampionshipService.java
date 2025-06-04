package pt.ul.fc.css.soccernow.services;

import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.ChampionshipRegistrationDTO;
import pt.ul.fc.css.soccernow.exceptions.ChampionshipNotFoundException;
import pt.ul.fc.css.soccernow.model.Championship;
import pt.ul.fc.css.soccernow.model.ChampionshipStatus;
import pt.ul.fc.css.soccernow.repository.ChampionshipRepository;

@Service
public class ChampionshipService {

  private final ChampionshipRepository championshipRepository;

  public ChampionshipService(ChampionshipRepository championshipRepository) {
    this.championshipRepository = championshipRepository;
  }

  // Register a new championship
  public Championship registerChampionship(ChampionshipRegistrationDTO dto) {
    validateChampionshipDTO(dto);

    Championship championship =
        new Championship(dto.getName(), dto.getStartDate(), dto.getLocation());

    championship.setDescription(dto.getDescription());
    championship.setEndDate(dto.getEndDate());
    championship.setStatus(ChampionshipStatus.PLANNED);

    return championshipRepository.save(championship);
  }

  // Get championship by ID
  public Championship getChampionshipById(Long id) {
    return championshipRepository
        .findById(id)
        .orElseThrow(() -> new ChampionshipNotFoundException(id));
  }

  // Get all championships
  public List<Championship> getAllChampionships() {
    return championshipRepository.findAll();
  }

  // Get championships by status
  public List<Championship> getChampionshipsByStatus(ChampionshipStatus status) {
    return championshipRepository.findByStatus(status);
  }

  public List<Championship> getPlannedChampionships() {
    return championshipRepository.findPlannedChampionships();
  }

  public List<Championship> getOngoingChampionships() {
    return championshipRepository.findOngoingChampionships();
  }

  public List<Championship> getCompletedChampionships() {
    return championshipRepository.findCompletedChampionships();
  }

  public List<Championship> getChampionshipsByTeam(Long teamId) {
    return championshipRepository.findByParticipatingTeamId(teamId);
  }

  // Update championship status
  public Championship updateStatus(Long id, ChampionshipStatus status) {
    Championship championship = getChampionshipById(id);
    championship.setStatus(status);
    return championshipRepository.save(championship);
  }

  // Delete a championship
  public void deleteChampionship(Long id) {
    Championship championship = getChampionshipById(id);
    championshipRepository.delete(championship);
  }

  // Helper validation
  private void validateChampionshipDTO(ChampionshipRegistrationDTO dto) {
    if (dto == null || dto.getName() == null || dto.getStartDate() == null) {
      throw new IllegalArgumentException("Championship name and start date are required");
    }
  }
}
