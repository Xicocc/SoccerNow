package pt.ul.fc.css.soccernow.dto;

import jakarta.validation.constraints.*;
import pt.ul.fc.css.soccernow.model.Position;

public class PlayerRegistrationDTO {
  @NotBlank(message = "Name is mandatory")
  private String name;

  @Min(value = 16, message = "Age must be at least 16")
  private Integer age;

  @NotNull(message = "Position is mandatory")
  private Position preferredPosition;

  public PlayerRegistrationDTO() {}

  public PlayerRegistrationDTO(String name, Integer age, Position preferredPosition) {
    this.name = name;
    this.age = age;
    this.preferredPosition = preferredPosition;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Position getPreferredPosition() {
    return preferredPosition;
  }

  public void setPreferredPosition(Position preferredPosition) {
    this.preferredPosition = preferredPosition;
  }
}
