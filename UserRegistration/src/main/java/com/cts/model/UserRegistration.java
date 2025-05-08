package com.cts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "User name cannot be blank")
	@Size(min = 4, max =20 , message = "User name must be between 4 and 20 characters")
	private String userName;

	@NotBlank(message = "User email cannot be blank")
	@Email(message = "User email should be valid")
	private String userEmail;

	@NotBlank(message = "User password cannot be blank")
	@Size(min = 8, message = "User password must be at least 8 characters long")
	private String userPassword;

	@NotBlank(message = "User contact number cannot be blank")
	@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "User contact number should be valid")
	private String userContactNumber;

}
