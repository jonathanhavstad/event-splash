package com.eventsplash.networking.components;

import com.eventsplash.networking.search.EventBriteSearchAPIService;
import com.eventsplash.networking.modules.EventBriteModule;
import com.eventsplash.networking.venue.EventBriteVenueAPIService;

import dagger.Component;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

@Component(modules = EventBriteModule.class)
public interface EventBriteComponent {
    EventBriteSearchAPIService providesEventBriteSearchApiService();
    EventBriteVenueAPIService providesEventBriteVenueAPIService();
}
