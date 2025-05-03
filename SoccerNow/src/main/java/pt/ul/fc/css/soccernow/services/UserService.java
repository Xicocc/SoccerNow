package pt.ul.fc.css.soccernow.service;

import java.util.List;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.soccernow.model.User;
import pt.ul.fc.css.soccernow.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getAllUsers() {
    return userRepository.findAllUsers();
  }

  public User getUserByName(String name) {
    return userRepository.findByName(name);
  }

  public boolean existsByName(String name) {
    return userRepository.findByName(name) != null;
  }
}
