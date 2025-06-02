package pt.ul.fc.css.soccernow.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.soccernow.dto.UserDTO;
import pt.ul.fc.css.soccernow.model.User;
import pt.ul.fc.css.soccernow.services.UserService;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User endpoints")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @Operation(summary = "Get all users (players and referees)")
  @ApiResponse(responseCode = "200", description = "List of all users")
  @GetMapping
  public ResponseEntity<List<UserDTO>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers().stream().map(UserDTO::fromUser).toList());
  }

  @Operation(summary = "Delete a user")
  @ApiResponse(responseCode = "200", description = "User deleted successfully")
  @ApiResponse(responseCode = "404", description = "User not found")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUserById(@PathVariable Long id, @PathVariable String name) {
    User user = userService.getUserByName(name);
    if (user.getId() == id) {
      if (userService.deleteUserById(id)) {
        return ResponseEntity.ok("User deleted successfully");
      }
    }
    return ResponseEntity.status(404).body("User not found");
  }
}
