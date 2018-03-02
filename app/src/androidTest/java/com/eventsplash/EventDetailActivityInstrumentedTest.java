package com.eventsplash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;


import com.eventsplash.eventdetail.EventDetailActivity;
import com.eventsplash.eventdetail.EventDetailBinder;
import com.eventsplash.eventdetail.models.EventWithVenue;
import com.eventsplash.model.eventbright.events.Event;
import com.eventsplash.model.eventbright.events.Name;
import com.eventsplash.model.eventbright.venues.Venue;

import org.junit.Rule;
import org.junit.Test;

/**
 * Created by jonathanhavstad on 3/2/18.
 */

public class EventDetailActivityInstrumentedTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<EventDetailActivity>(
            EventDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            EventWithVenue eventWithVenue = new EventWithVenue();
            Event event = new Event();
            Name name = new Name();
            name.setText("Test Event");
            event.setName(name);
            eventWithVenue.setEvent(event);
            Venue venue = new Venue();
            eventWithVenue.setVenue(venue);
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, EventDetailActivity.class);
            Bundle args = new Bundle();
            args.putBinder("event_detail_binder",
                    new EventDetailBinder(eventWithVenue));
            result.putExtra("event_detail_bundle", args);
            return result;
        }
    };

    @Test
    public void validateFragmentPlaceholders() throws Exception {
        Espresso.onView(ViewMatchers.withId(R.id.event_detail_content))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
