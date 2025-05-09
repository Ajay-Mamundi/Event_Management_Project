package com.cts.model;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Tuple;
import jakarta.validation.constraints.NotNull;
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
public class FeedbackAndRatings {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull(message = "Event ID cannot be null")
	private int eventId;
	@NotNull(message = "User ID cannot be null")
	private int userId;
	@Range(min = 1, max = 5, message = "Rating must be between 1 and 5")
	private Double rating;
	@Size(max = 500, message = "Comments cannot exceed 500 characters")
	private String comments;
	private LocalDateTime submittedTimestamp;
}
