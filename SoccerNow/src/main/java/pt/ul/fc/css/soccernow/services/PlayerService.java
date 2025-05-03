package pt.ul.fc.css.soccernow.services;

import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.dto.PlayerRegistrationDTO;
import pt.ul.fc.css.soccernow.exceptions.PlayerNotFoundException;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Position;
import pt.ul.fc.css.soccernow.repository.PlayerRepository;

@Service
public class PlayerService {
  private final PlayerRepository playerRepository;
  private final UserService userService;

  public PlayerService(PlayerRepository playerRepository, UserService userService) {
    this.playerRepository = playerRepository;
    this.userService = userService;
  }

  public Player registerPlayer(PlayerRegistrationDTO dto) {
    if (userService.existsByName(dto.getName())) {
      throw new IllegalArgumentException("Player name already exists");
    }

    Player player = new Player();
    player.setName(dto.getName());
    player.setAge(dto.getAge());
    player.setPreferredPosition(dto.getPreferredPosition());
    return playerRepository.save(player);
  }

  public List<Player> getAllPlayers() {
    return playerRepository.findAllPlayers();
  }

  public Double getAverageGoals(String playerName) {
    Double avg = playerRepository.findAverageGoalsPerGameByName(playerName);
    return avg != null ? avg : 0.0;
  }

  public List<Player> getPlayersByPosition(Position position) {
    return playerRepository.findByPosition(position);
  }

  public List<Player> getPlayersWithMostRedCards() {
    return playerRepository.findPlayersByMostRedCards();
  }

  public Player addGoals(Long playerId, int goals) {
    Player player =
        (Player)
            playerRepository
                .findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

    if (goals < 0) {
      throw new IllegalArgumentException("Goals cannot be negative");
    }

    player.setGoalsScored(player.getGoalsScored() + goals);

    return playerRepository.save(player);
  }

  public Player addYellowCards(Long playerId, int ycards) {
    Player player =
        (Player)
            playerRepository
                .findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

    if (ycards < 0) {
      throw new IllegalArgumentException("Cards cannot be negative");
    }

    player.setYellowCards(player.getYellowCards() + ycards);

    return playerRepository.save(player);
  }

  public Player addRedCards(Long playerId, int rcards) {
    Player player =
        (Player)
            playerRepository
                .findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

    if (rcards < 0) {
      throw new IllegalArgumentException("Cards cannot be negative");
    }

    player.setRedCards(player.getRedCards() + rcards);

    return playerRepository.save(player);
  }

  public Player addGamesPlayed(Long playerId, int games) {
    Player player =
        (Player)
            playerRepository
                .findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

    if (games < 0) {
      throw new IllegalArgumentException("Games cannot be negative");
    }

    player.setGamesPlayed(player.getGamesPlayed() + games);

    return playerRepository.save(player);
  }
}
