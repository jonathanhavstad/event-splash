package com.eventsplash.eventdetail.models;

import com.eventsplash.model.eventbright.events.Event;
import com.eventsplash.model.eventbright.venues.Venue;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class EventWithVenue {
    private Event event;
    private Venue venue;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
