package com.cts.exceptions;

public class FeedbackAndRatingsNotFoundException extends Exception {
	   public FeedbackAndRatingsNotFoundException(int id) {
		  super("Feedback not found with id: " + id);
	}

}
