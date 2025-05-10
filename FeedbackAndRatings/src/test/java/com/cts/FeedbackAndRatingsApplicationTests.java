package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.dto.EventManagement;
import com.cts.dto.UserRegistration;
import com.cts.exceptions.FeedbackAndRatingsNotFoundException;
import com.cts.feignclient.EventManagementClient;
import com.cts.feignclient.UserRegistrationClient;
import com.cts.model.FeedbackAndRatings;
import com.cts.repository.FeedbackAndRatingsRepository;
import com.cts.service.FeedbackAndRatingsServiceImpl;

class FeedbackAndRatingsServiceImplTest {

    @Mock
    private FeedbackAndRatingsRepository repository;

    @Mock
    private EventManagementClient eventClient;

   
    private UserRegistrationClient userClient;

    @InjectMocks
    private FeedbackAndRatingsServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveFeedback() {
        FeedbackAndRatings feedback = new FeedbackAndRatings();
        feedback.setEventId(1);
        feedback.setUserId(1);

        EventManagement event = new EventManagement();
        UserRegistration user = new UserRegistration();

        when(eventClient.getEventById(1)).thenReturn(event);
        when(userClient.getUserById(1)).thenReturn(user);
        when(repository.save(feedback)).thenReturn(feedback);

        String result = service.saveFeedback(feedback);
        assertEquals("Feedback saved successfully", result);
        verify(repository, times(1)).save(feedback);
    }

    @Test
    void testSaveFeedbackInvalidUser() {
        FeedbackAndRatings feedback = new FeedbackAndRatings();
        feedback.setEventId(1);
        feedback.setUserId(1);

        EventManagement event = new EventManagement();

        when(eventClient.getEventById(1)).thenReturn(event);
        when(userClient.getUserById(1)).thenReturn(null);

        String result = service.saveFeedback(feedback);
        assertEquals("Enter valid userId", result);
        verify(repository, never()).save(feedback);
    }

    @Test
    void testUpdateFeedback() throws FeedbackAndRatingsNotFoundException {
        FeedbackAndRatings feedback = new FeedbackAndRatings();
        feedback.setRating(4.5);
        feedback.setComments("Great event!");

        FeedbackAndRatings existingFeedback = new FeedbackAndRatings();
        existingFeedback.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(existingFeedback));
        when(repository.save(existingFeedback)).thenReturn(existingFeedback);

        String result = service.updateFeedback(1, feedback);
        assertEquals("Feedback updated successfully", result);
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(existingFeedback);
    }

    @Test
    void testUpdateFeedbackNotFound() {
        FeedbackAndRatings feedback = new FeedbackAndRatings();
        feedback.setRating(4.5);
        feedback.setComments("Great event!");

        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(FeedbackAndRatingsNotFoundException.class, () -> service.updateFeedback(1, feedback));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testDeleteFeedback() throws FeedbackAndRatingsNotFoundException {
        FeedbackAndRatings feedback = new FeedbackAndRatings();
        feedback.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(feedback));

        String result = service.deleteFeedback(1);
        assertEquals("Feedback deleted successfully", result);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteFeedbackNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(FeedbackAndRatingsNotFoundException.class, () -> service.deleteFeedback(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetFeedbackById() throws FeedbackAndRatingsNotFoundException {
        FeedbackAndRatings feedback = new FeedbackAndRatings();
        feedback.setId(1);

        when(repository.findById(1)).thenReturn(Optional.of(feedback));

        FeedbackAndRatings result = service.getFeedbackById(1);
        assertEquals(feedback, result);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetFeedbackByIdNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(FeedbackAndRatingsNotFoundException.class, () -> service.getFeedbackById(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetAllFeedbacksByEvent() {
        List<FeedbackAndRatings> feedbacks = Arrays.asList(new FeedbackAndRatings(), new FeedbackAndRatings());
        when(repository.findByEventId(1)).thenReturn(feedbacks);

        List<FeedbackAndRatings> result = service.getAllFeedbacksByEvent(1);
        assertEquals(feedbacks, result);
        verify(repository, times(1)).findByEventId(1);
    }

    @Test
    void testGetAverageRatingForEvent() {
        when(repository.findAverageRatingByEventId(1)).thenReturn(4.5);

        Double result = service.getAverageRatingForEvent(1);
        assertEquals(4.5, result);
        verify(repository, times(1)).findAverageRatingByEventId(1);
    }

    @Test
    void testGetAllFeedbacks() {
        List<FeedbackAndRatings> feedbacks = Arrays.asList(new FeedbackAndRatings(), new FeedbackAndRatings());
        when(repository.findAll()).thenReturn(feedbacks);

        List<FeedbackAndRatings> result = service.getAllfeedbacks();
        assertEquals(feedbacks, result);
        verify(repository, times(1)).findAll();
    }
}

