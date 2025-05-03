package pt.ul.fc.css.soccernow.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.ul.fc.css.soccernow.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByName(String name);

  @Query("SELECT u FROM User u")
  List<User> findAllUsers();
}
