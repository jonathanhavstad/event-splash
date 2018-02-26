package com.eventsplash.networking;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eventsplash.R;
import com.eventsplash.model.eventbright.EventWithVenue;
import com.eventsplash.model.eventbright.events.Event;
import com.eventsplash.model.eventbright.events.SearchResults;
import com.eventsplash.model.eventbright.venues.Venue;
import com.eventsplash.networking.components.DaggerEventBriteComponent;
import com.eventsplash.networking.components.EventBriteComponent;
import com.eventsplash.networking.modules.EventBriteModule;
import com.eventsplash.networking.search.EventBriteSearchAPIService;
import com.eventsplash.networking.venue.EventBriteVenueAPIService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class EventBriteRequestManager {
    private EventBriteComponent eventBriteComponent;
    private EventBriteSearchAPIService eventBriteSearchAPIService;
    private EventBriteVenueAPIService eventBriteVenueAPIService;

    private Map<String, Event> venueIDToEventMap;

    public EventBriteRequestManager(Context context) {
        eventBriteComponent =
                DaggerEventBriteComponent.builder()
                        .eventBriteModule(new EventBriteModule(context.getString(R.string.event_brite_base_url),
                                context.getString(R.string.event_brite_auth_token),
                                context.getString(R.string.event_brite_search_api_path),
                                context.getString(R.string.event_brite_venue_api_path)))
                        .build();
        eventBriteSearchAPIService = eventBriteComponent.providesEventBriteSearchApiService();
        eventBriteVenueAPIService = eventBriteComponent.providesEventBriteVenueAPIService();
    }

    public boolean isRequestingSearch() {
        return eventBriteSearchAPIService.isRequestingSearch();
    }

    public boolean isRequestingVenueInformation() {
        return eventBriteVenueAPIService.isRequestingVenueInformation();
    }

    public Flowable<SearchResults> requestSearch() {
        return eventBriteSearchAPIService.searchAll();
    }

    public Flowable<SearchResults> requestSearchByLatLon(double latitude,
                                                         double longitude,
                                                         int searchRadius) {
        return eventBriteSearchAPIService.searchByLatLon(latitude,
                longitude,
                searchRadius);
    }

    public Flowable<Venue> requestVenueInformation(String venueID) {
        return eventBriteVenueAPIService.getVenueInformation(venueID);
    }

    public Flowable<List<EventWithVenue>> requestVenueInformationList(@NonNull Map<String, Event> venueIDToEventMap) {
        this.venueIDToEventMap = venueIDToEventMap;
        List<Flowable<Venue>> venueRequests = new ArrayList<>();
        for (String venueID : venueIDToEventMap.keySet()) {
            venueRequests.add(eventBriteVenueAPIService.getVenueInformation(venueID));
        }
        return Flowable.zip(venueRequests, this::processVenueRequests);
    }

    private List<EventWithVenue> processVenueRequests(Object[] objects) throws Exception {
        List<EventWithVenue> venueList = new ArrayList<>();
        for (Object obj : objects) {
            if (obj instanceof Venue) {
                Venue venue = (Venue) obj;
                EventWithVenue eventWithVenue = new EventWithVenue();
                eventWithVenue.setEvent(venueIDToEventMap.get(venue.getId()));
                eventWithVenue.setVenue(venue);
                venueList.add(eventWithVenue);
            }
        }
        return venueList;
    }
}
