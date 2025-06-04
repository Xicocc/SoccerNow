package pt.ul.fc.css.soccernow.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ChampionshipRegistrationDTO {

  @NotBlank(message = "Championship name is required")
  private String name;

  private String description;

  @NotNull(message = "Start date is required")
  @FutureOrPresent(message = "Start date must be today or in the future")
  private LocalDate startDate;

  private LocalDate endDate;

  private String location;

  public ChampionshipRegistrationDTO() {}

  public ChampionshipRegistrationDTO(
      String name, String description, LocalDate startDate, LocalDate endDate, String location) {
    this.name = name;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
    this.location = location;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
