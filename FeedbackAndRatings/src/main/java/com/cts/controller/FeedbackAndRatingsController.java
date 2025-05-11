package com.cts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.exceptions.FeedbackAndRatingsNotFoundException;
import com.cts.model.FeedbackAndRatings;
import com.cts.service.FeedbackAndRatingsService;

@RestController
@RequestMapping("/feedbackandratings")
public class FeedbackAndRatingsController {
	@Autowired
	FeedbackAndRatingsService service;

	@PostMapping("/save")
	public String saveFeedback(@RequestBody FeedbackAndRatings feedback) {
		return service.saveFeedback(feedback);
	}

	@PutMapping("/update/{id}")
	public String updateFeedback(@PathVariable("id") int id, @RequestBody FeedbackAndRatings feedback)
			throws FeedbackAndRatingsNotFoundException {
		return service.updateFeedback(id, feedback);
	}

	@DeleteMapping("/delete/{id}")
	public String deleteFeedback(@PathVariable("id") int id) throws FeedbackAndRatingsNotFoundException {
		return service.deleteFeedback(id);
	}

	@GetMapping("getfeedbackbyid/{id}")
	public FeedbackAndRatings getFeedbackById(@PathVariable("id") int id) throws FeedbackAndRatingsNotFoundException {
		return service.getFeedbackById(id);
	}

	@GetMapping("/getfeedbackbyeventid/{eventid}")
	public List<FeedbackAndRatings> getAllFeedbacksByEvent(@PathVariable("eventid") int eventId) {
		return service.getAllFeedbacksByEvent(eventId);
	}

	@GetMapping("/eventavg/{eventid}")
	public Double getAverageRatingForEvent(@PathVariable("eventid") int eventId) {
		return service.getAverageRatingForEvent(eventId);
	}

	@GetMapping("/fetchallfeedback")
	public List<FeedbackAndRatings> getAllfeedbacks() {
		return service.getAllfeedbacks();

	}

}
