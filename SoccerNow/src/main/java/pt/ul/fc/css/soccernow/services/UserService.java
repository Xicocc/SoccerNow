package pt.ul.fc.css.soccernow.service;

import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.model.User;
import pt.ul.fc.css.soccernow.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getUserByName(String name) {
    return userRepository.findByName(name);
  }

  public boolean existsByName(String name) {
    return userRepository.findByName(name) != null;
  }
}
