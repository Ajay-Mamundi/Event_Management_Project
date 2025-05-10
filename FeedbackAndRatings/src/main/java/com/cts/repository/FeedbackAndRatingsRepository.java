package com.cts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cts.model.FeedbackAndRatings;

public interface FeedbackAndRatingsRepository extends JpaRepository<FeedbackAndRatings, Integer> {

	List<FeedbackAndRatings> findByUserId(int userId);

	List<FeedbackAndRatings> findByEventId(int eventId);


   // @Query("SELECT AVG(f.rating) FROM FeedbackAndRatings f WHERE f.eventId = :eventId")
    Double findAverageRatingByEventId(int eventId);

	//Optional<FeedbackAndRatings> findById();

}
