package com.cts.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.event.spi.EventManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.cts.dto.EventManagement;
import com.cts.exceptions.FeedbackAndRatingsNotFoundException;
import com.cts.feignclient.EventManagementClient;
import com.cts.model.FeedbackAndRatings;
import com.cts.repository.FeedbackAndRatingsRepository;

@Service
public class FeedbackAndRatingsServiceImpl implements FeedbackAndRatingsService {
	@Autowired
	FeedbackAndRatingsRepository repository;

	@Autowired
	EventManagementClient eventClient;

	@Override
	public String saveFeedback(FeedbackAndRatings feedback) {
		EventManagement eventId = eventClient.getEventById(feedback.getEventId());
		if (eventId == null) {
			return "Enter valid EventId";
		}
		feedback.setSubmittedTimestamp(LocalDateTime.now());
		repository.save(feedback);
		return "Feedback saved successfully";
	}

	@Override
	public String updateFeedback(int feedbackId, FeedbackAndRatings feedback)
			throws FeedbackAndRatingsNotFoundException {
		Optional<FeedbackAndRatings> existingFeedback = repository.findById(feedbackId);
		if (existingFeedback.isPresent()) {
			FeedbackAndRatings updatedFeedback = existingFeedback.get();
			updatedFeedback.setRating(feedback.getRating());
			updatedFeedback.setComments(feedback.getComments());
			repository.save(updatedFeedback);
			return "Feedback updated successfully";
		} else {
			throw new FeedbackAndRatingsNotFoundException(feedbackId);
		}

	}

	@Override
	public String deleteFeedback(int feedbackId) {
		repository.deleteById(feedbackId);
		return "Feedback deleted successfully";
	}

	@Override
	public FeedbackAndRatings getFeedbackById(int feedbackId) throws FeedbackAndRatingsNotFoundException {
		Optional<FeedbackAndRatings> optional = repository.findById(feedbackId);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new FeedbackAndRatingsNotFoundException(feedbackId);

	}

	@Override
	public List<FeedbackAndRatings> getAllFeedbacksByUser(int userId) {
		return repository.findByUserId(userId);
	}

	@Override
	public List<FeedbackAndRatings> getAllFeedbacksByEvent(int eventId) {
		return repository.findByEventId(eventId);
	}

	@Override
	public Double getAverageRatingForEvent(int eventId) {
		return repository.findAverageRatingByEventId(eventId);
	}

	@Override
	public List<FeedbackAndRatings> getAllfeedbacks() {
		return repository.findAll();
	}

}
