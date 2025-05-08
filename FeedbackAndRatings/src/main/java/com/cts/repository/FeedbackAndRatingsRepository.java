package com.cts.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.model.FeedbackAndRatings;

import jakarta.persistence.Tuple;

public interface FeedbackAndRatingsRepository extends JpaRepository<FeedbackAndRatings, Integer> {

	List<FeedbackAndRatings> findByUserId(int userId);

	List<FeedbackAndRatings> findByEventId(int eventId);

	Double findAverageRatingByEventId(int eventId);

	//Optional<FeedbackAndRatings> findById();

}
