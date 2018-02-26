package com.eventsplash.home;

import com.eventsplash.base.Lifecycle;
import com.eventsplash.model.eventbright.events.Event;

import java.util.List;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class HomeContract {
    public interface View extends Lifecycle.View {
        void onSuccess(Object next);
        void onFailure();
    }

    public interface ViewModel extends Lifecycle.ViewModel {
        void requestSearch();
        void requestSearchByLatLon(double latitude, double longitude, int radius);
        void requestVenueInformation(String venueID);
        void requestVenueInformationList(List<Event> eventList);
    }
}
