package com.cts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.exceptions.EventNotFoundException;
import com.cts.exceptions.InvalidDataException;
import com.cts.model.EventManagement;
import com.cts.repository.EventManagementRepository;
import com.cts.service.EventManagementServiceImpl;

class EventManagementServiceImplTest {

    @Mock
    private EventManagementRepository repository;

    @InjectMocks
    private EventManagementServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEvent() throws InvalidDataException {
        EventManagement event = new EventManagement();
        when(repository.save(event)).thenReturn(event);

        String result = service.saveEvent(event);
        assertEquals("Event created successfully..!!", result);
        verify(repository, times(1)).save(event);
    }

    @Test
    void testSaveEventInvalidData() {
        EventManagement event = new EventManagement();
        when(repository.save(event)).thenReturn(null);

        assertThrows(InvalidDataException.class, () -> service.saveEvent(event));
        verify(repository, times(1)).save(event);
    }

    @Test
    void testGetEventById() throws EventNotFoundException {
        EventManagement event = new EventManagement();
        when(repository.findById(1)).thenReturn(Optional.of(event));

        EventManagement result = service.getEventById(1);
        assertEquals(event, result);
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetEventByIdNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> service.getEventById(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testUpdateEvent() throws InvalidDataException {
        EventManagement existingEvent = new EventManagement();
        EventManagement updatedEvent = new EventManagement();
        when(repository.findById(1)).thenReturn(Optional.of(existingEvent));
        when(repository.save(existingEvent)).thenReturn(existingEvent);

        String result = service.updateEvent(1, updatedEvent);
        assertEquals("Event updated successfully..!", result);
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(existingEvent);
    }

    @Test
    void testUpdateEventNotFound() {
        EventManagement updatedEvent = new EventManagement();
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InvalidDataException.class, () -> service.updateEvent(1, updatedEvent));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testDeleteEventById() throws EventNotFoundException {
        when(repository.existsById(1)).thenReturn(true);

        String result = service.deleteEventById(1);
        assertEquals("Event deleted successfully", result);
        verify(repository, times(1)).existsById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteEventByIdNotFound() {
        when(repository.existsById(1)).thenReturn(false);

        assertThrows(EventNotFoundException.class, () -> service.deleteEventById(1));
        verify(repository, times(1)).existsById(1);
    }

    @Test
    void testGetAllEvents() {
        List<EventManagement> events = Arrays.asList(new EventManagement(), new EventManagement());
        when(repository.findAll()).thenReturn(events);

        List<EventManagement> result = service.getAllEvents();
        assertEquals(events, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetEventByCategory() throws EventNotFoundException {
        List<EventManagement> events = Arrays.asList(new EventManagement(), new EventManagement());
        when(repository.findByEventCategory("Music")).thenReturn(events);

        List<EventManagement> result = service.getEventByCategory("Music");
        assertEquals(events, result);
        verify(repository, times(1)).findByEventCategory("Music");
    }

    @Test
    void testGetEventByCategoryNotFound() {
        when(repository.findByEventCategory("Music")).thenReturn(Arrays.asList());

        assertThrows(EventNotFoundException.class, () -> service.getEventByCategory("Music"));
        verify(repository, times(1)).findByEventCategory("Music");
    }

    @Test
    void testDecreaseTicketCount() throws EventNotFoundException {
        EventManagement event = new EventManagement();
        event.setTicketCount(10);
        when(repository.findById(1)).thenReturn(Optional.of(event));
        when(repository.save(event)).thenReturn(event);

        service.decreaseTicketCount(1);
        assertEquals(9, event.getTicketCount());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(event);
    }

    @Test
    void testDecreaseTicketCountNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> service.decreaseTicketCount(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testIncreaseTicketCount() throws EventNotFoundException {
        EventManagement event = new EventManagement();
        event.setTicketCount(10);
        when(repository.findById(1)).thenReturn(Optional.of(event));
        when(repository.save(event)).thenReturn(event);

        service.increaseTicketCount(1);
        assertEquals(11, event.getTicketCount());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(event);
    }

    @Test
    void testIncreaseTicketCountNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> service.increaseTicketCount(1));
        verify(repository, times(1)).findById(1);
    }

    @Test
    void testGetEventByLocation() throws EventNotFoundException {
        List<EventManagement> events = Arrays.asList(new EventManagement(), new EventManagement());
        when(repository.findByEventLocation("New York")).thenReturn(events);

        List<EventManagement> result = service.getEventByLocation("New York");
        assertEquals(events, result);
        verify(repository, times(1)).findByEventLocation("New York");
    }

    @Test
    void testGetEventByLocationNotFound() {
        when(repository.findByEventLocation("New York")).thenReturn(Arrays.asList());

        assertThrows(EventNotFoundException.class, () -> service.getEventByLocation("New York"));
        verify(repository, times(1)).findByEventLocation("New York");
    }

    @Test
    void testGetEventByDate() throws EventNotFoundException {
        Date date = new Date();
        List<EventManagement> events = Arrays.asList(new EventManagement(), new EventManagement());
        when(repository.findByEventDate(date)).thenReturn(events);

        List<EventManagement> result = service.getEventByDate(date);
        assertEquals(events, result);
        verify(repository, times(1)).findByEventDate(date);
    }

    @Test
    void testGetEventByDateNotFound() {
        Date date = new Date();
        when(repository.findByEventDate(date)).thenReturn(Arrays.asList());

        assertThrows(EventNotFoundException.class, () -> service.getEventByDate(date));
        verify(repository, times(1)).findByEventDate(date);
    }
}
