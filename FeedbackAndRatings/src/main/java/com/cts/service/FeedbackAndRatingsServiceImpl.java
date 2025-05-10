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
        if (eventId == null) {
            logger.error("Event not found with ID: {}", feedback.getEventId());
            return "Enter valid EventId";
        }

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
        Optional<FeedbackAndRatings> existingFeedback = repository.findById(feedbackId);
        if (existingFeedback.isPresent()) {
            FeedbackAndRatings updatedFeedback = existingFeedback.get();
            updatedFeedback.setRating(feedback.getRating());
            updatedFeedback.setComments(feedback.getComments());
            repository.save(updatedFeedback);
            logger.info("Feedback updated successfully with ID: {}", feedbackId);
            return "Feedback updated successfully";
        } else {
            logger.error("Feedback not found with ID: {}", feedbackId);
            throw new FeedbackAndRatingsNotFoundException(feedbackId);
        }
    }


    @Override
    public String deleteFeedback(int feedbackId) {
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
            throw new FeedbackAndRatingsNotFoundException(feedbackId);
        }
    }


    @Override
    public List<FeedbackAndRatings> getAllFeedbacksByUser(int userId) {
        logger.info("Fetching all feedbacks for user ID: {}", userId);
        return repository.findByUserId(userId);
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
