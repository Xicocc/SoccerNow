package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.ul.fc.css.soccernow.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByName(String name);

  boolean existsByName(String name);

  @Query("SELECT u FROM User u")
  List<User> findAllUsers();

  Optional<User> findById(Long id);
}
