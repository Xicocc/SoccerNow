package pt.ul.fc.css.soccernow.dto;

import jakarta.validation.constraints.*;
import pt.ul.fc.css.soccernow.model.Player;
import pt.ul.fc.css.soccernow.model.Referee;
import pt.ul.fc.css.soccernow.model.User;

public record UserDTO(Long id, String name, Integer age, String userType) {
  public static UserDTO fromUser(User user) {
    if (user instanceof Player) {
      return new UserDTO(user.getId(), user.getName(), user.getAge(), "PLAYER");
    } else if (user instanceof Referee) {
      return new UserDTO(user.getId(), user.getName(), user.getAge(), "REFEREE");
    }
    throw new IllegalStateException("Unknown user type");
  }
}
