package pt.ul.fc.css.soccernow.dto;

import jakarta.validation.constraints.NotBlank;

public class TeamRegistrationDTO {
  @NotBlank(message = "Team name is mandatory")
  private String name;

  public TeamRegistrationDTO() {}

  public TeamRegistrationDTO(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
