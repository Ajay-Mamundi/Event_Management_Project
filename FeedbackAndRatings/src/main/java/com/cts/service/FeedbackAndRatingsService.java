package com.cts.service;

import java.util.List;

import com.cts.exceptions.FeedbackAndRatingsNotFoundException;
import com.cts.model.FeedbackAndRatings;

import jakarta.persistence.Tuple;

public interface FeedbackAndRatingsService {
public abstract String saveFeedback(FeedbackAndRatings feedback);
    
    public abstract String updateFeedback(int feedbackId, FeedbackAndRatings feedback) throws FeedbackAndRatingsNotFoundException;
    
    public abstract String deleteFeedback(int feedbackId);
    
    public abstract FeedbackAndRatings getFeedbackById(int feedbackId) throws FeedbackAndRatingsNotFoundException;
    
    public abstract List<FeedbackAndRatings> getAllFeedbacksByUser(int userId);
    
    public abstract List<FeedbackAndRatings> getAllfeedbacks();
    
    public abstract List<FeedbackAndRatings> getAllFeedbacksByEvent(int eventId);
    
    public abstract Double getAverageRatingForEvent(int eventId);
}
