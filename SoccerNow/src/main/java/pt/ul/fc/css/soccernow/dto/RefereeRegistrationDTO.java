package pt.ul.fc.css.soccernow.dto;

import jakarta.validation.constraints.*;

public class RefereeRegistrationDTO {
  @NotBlank(message = "Name is mandatory")
  private String name;

  @Min(value = 18, message = "Referees must be at least 18 years old")
  private Integer age;

  public RefereeRegistrationDTO() {}

  public RefereeRegistrationDTO(String name, Integer age) {
    this.name = name;
    this.age = age;
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
}
