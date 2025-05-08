package com.cts.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventManagement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "Event name is mandatory")
	private String eventName;
	@NotBlank(message = "Event category is mandatory")
	private String eventCategory;
	@NotBlank(message = "Event location is mandatory")
	private String eventLocation;

	@Future(message = "Event date must be in the future")
	@NotNull(message = "Event date is mandatory")
	private Date eventDate;

	@Min(value = 1, message = "Event organizer ID must be at least 1")
	private int eventOrganizerId;

	@Min(value = 1, message = "Ticket count must be at least 1")
	private int ticketCount;

	@PrePersist
	@PreUpdate
	public void formatToLowerCase() {
		if (eventName != null) {
			eventName = eventName.toLowerCase();
		}

		if (eventCategory != null) {
			eventCategory = eventCategory.toLowerCase();
		}

		if (eventLocation != null) {
			eventLocation = eventLocation.toLowerCase();
		}
	}
}
