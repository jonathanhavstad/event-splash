package com.eventsplash.eventdetail.handlers;

import android.view.View;

import com.eventsplash.eventdetail.models.EventWithVenue;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

public class EventWithVenueClickHandler implements View.OnClickListener {
    public interface OnSelectEventWithVenue {
        void selectEventWithVenue(EventWithVenue eventWithVenue);
    }

    private EventWithVenue eventWithVenue;
    private final OnSelectEventWithVenue onSelectEventWithVenue;

    public EventWithVenueClickHandler(OnSelectEventWithVenue onSelectEventWithVenue) {
        this.onSelectEventWithVenue = onSelectEventWithVenue;
    }

    @Override
    public void onClick(View v) {
        onSelectEventWithVenue.selectEventWithVenue(eventWithVenue);
    }

    public EventWithVenue getEventWithVenue() {
        return eventWithVenue;
    }

    public void setEventWithVenue(EventWithVenue eventWithVenue) {
        this.eventWithVenue = eventWithVenue;
    }

    public OnSelectEventWithVenue getOnSelectEventWithVenue() {
        return onSelectEventWithVenue;
    }
}
