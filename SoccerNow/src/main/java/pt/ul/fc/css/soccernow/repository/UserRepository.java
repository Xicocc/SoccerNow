package pt.ul.fc.css.soccernow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.soccernow.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByName(String name);
}
