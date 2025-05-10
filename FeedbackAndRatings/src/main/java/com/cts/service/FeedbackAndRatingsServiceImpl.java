package com.cts.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cts.dto.EventManagement;
import com.cts.dto.UserRegistration;
import com.cts.exceptions.FeedbackAndRatingsNotFoundException;
import com.cts.feignclient.EventManagementClient;
import com.cts.feignclient.UserRegistrationClient;
import com.cts.model.FeedbackAndRatings;
import com.cts.repository.FeedbackAndRatingsRepository;

@Service
public class FeedbackAndRatingsServiceImpl implements FeedbackAndRatingsService {

    // Logger instance for logging
    private static final Logger logger = LoggerFactory.getLogger(FeedbackAndRatingsServiceImpl.class);

    @Autowired
    FeedbackAndRatingsRepository repository;

    @Autowired
    EventManagementClient eventClient;
    
    @Autowired
    UserRegistrationClient userClient;


    @Override
    public String saveFeedback(FeedbackAndRatings feedback) {
        logger.info("Saving feedback for event ID: {} and user ID: {}", feedback.getEventId(), feedback.getUserId());

        // Check if the event exists
        EventManagement eventId = eventClient.getEventById(feedback.getEventId());

        // Check if the user exists
        UserRegistration userId = userClient.getUserById(feedback.getUserId());
        if (userId == null) {
            logger.error("User not found with ID: {}", feedback.getUserId());
            return "Enter valid userId";
        }

        // Set the submitted timestamp and save the feedback
        feedback.setSubmittedTimestamp(LocalDateTime.now());
        repository.save(feedback);
        logger.info("Feedback saved successfully for event ID: {} and user ID: {}", feedback.getEventId(), feedback.getUserId());
        return "Feedback saved successfully";
    }


    @Override
    public String updateFeedback(int feedbackId, FeedbackAndRatings feedback)
            throws FeedbackAndRatingsNotFoundException {
        logger.info("Updating feedback with ID: {}", feedbackId);
        FeedbackAndRatings existingFeedback = getFeedbackById(feedbackId);
        FeedbackAndRatings updatedFeedback = existingFeedback;
        updatedFeedback.setRating(feedback.getRating());
        updatedFeedback.setComments(feedback.getComments());
        repository.save(updatedFeedback);
        logger.info("Feedback updated successfully with ID: {}", feedbackId);
        return "Feedback updated successfully";
  
    }


    @Override
    public String deleteFeedback(int feedbackId) throws FeedbackAndRatingsNotFoundException {
    	
    	FeedbackAndRatings feedback = getFeedbackById(feedbackId);
    	
        logger.info("Deleting feedback with ID: {}", feedbackId);
        repository.deleteById(feedbackId);
        logger.info("Feedback deleted successfully with ID: {}", feedbackId);
        return "Feedback deleted successfully";
    }


    @Override
    public FeedbackAndRatings getFeedbackById(int feedbackId) throws FeedbackAndRatingsNotFoundException {
        logger.info("Fetching feedback with ID: {}", feedbackId);
        Optional<FeedbackAndRatings> optional = repository.findById(feedbackId);
        if (optional.isPresent()) {
            logger.info("Feedback found with ID: {}", feedbackId);
            return optional.get();
        } else {
            logger.error("Feedback not found with ID: {}", feedbackId);
            throw new FeedbackAndRatingsNotFoundException("Feedback not found with id: " + feedbackId);
        }
    }

    @Override
    public List<FeedbackAndRatings> getAllFeedbacksByEvent(int eventId) {
        logger.info("Fetching all feedbacks for event ID: {}", eventId);
        return repository.findByEventId(eventId);
    }


    @Override
    public Double getAverageRatingForEvent(int eventId) {
        logger.info("Fetching average rating for event ID: {}", eventId);
        return repository.findAverageRatingByEventId(eventId);
    }

  
    @Override
    public List<FeedbackAndRatings> getAllfeedbacks() {
        logger.info("Fetching all feedbacks");
        return repository.findAll();
    }
}
