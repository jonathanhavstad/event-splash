package com.eventsplash.eventdetail;

import android.os.Binder;

import com.eventsplash.eventdetail.models.EventWithVenue;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

public class EventDetailBinder extends Binder {
    private EventWithVenue eventWithVenue;

    public EventDetailBinder(EventWithVenue eventWithVenue) {
        this.eventWithVenue = eventWithVenue;
    }

    public EventWithVenue getEventWithVenue() {
        return eventWithVenue;
    }

    public void setEventWithVenue(EventWithVenue eventWithVenue) {
        this.eventWithVenue = eventWithVenue;
    }
}
