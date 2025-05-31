package pt.ul.fc.css.soccernow.services;

import java.util.List;
import java.util.Optional;
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

  public boolean deleteUserById(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.delete(user.get());
      return true;
    }
    return false;
  }
}
