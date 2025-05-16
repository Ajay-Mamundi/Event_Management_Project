package com.cts.dto;

import java.sql.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventManagement {
	
	private int eventid;
	private String eventName;
	private String eventCategory;
	private String eventLocation;

	private Date eventDate;
	

}
