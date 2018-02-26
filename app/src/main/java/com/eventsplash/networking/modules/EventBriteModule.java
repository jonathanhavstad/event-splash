package com.eventsplash.networking.modules;

import com.eventsplash.networking.retrofit.EventBriteAPI;
import com.eventsplash.networking.search.EventBriteSearchAPIService;
import com.eventsplash.networking.venue.EventBriteVenueAPIService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

@Module
public class EventBriteModule {
    private final String eventBriteApiBaseUrl;
    private final String eventBriteAuthToken;
    private final String eventBriteSearchAPIPath;
    private final String eventBriteVenueAPIPath;

    public EventBriteModule(String eventBriteApiBaseUrl,
                            String eventBriteAuthToken,
                            String eventBriteSearchAPIPath,
                            String eventBriteVenueAPIPath) {
        this.eventBriteApiBaseUrl = eventBriteApiBaseUrl;
        this.eventBriteAuthToken = eventBriteAuthToken;
        this.eventBriteSearchAPIPath = eventBriteSearchAPIPath;
        this.eventBriteVenueAPIPath = eventBriteVenueAPIPath;
    }

    @Provides
    public Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(eventBriteApiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    public EventBriteAPI eventBriteApi(Retrofit retrofit) {
        return retrofit.create(EventBriteAPI.class);
    }

    @Provides
    public EventBriteSearchAPIService eventBriteSearchApiService(EventBriteAPI eventBriteAPI) {
        return new EventBriteSearchAPIService(eventBriteAPI,
                eventBriteAuthToken,
                eventBriteSearchAPIPath);
    }

    @Provides
    public EventBriteVenueAPIService eventBriteVenueAPIService(EventBriteAPI eventBriteAPI) {
        return new EventBriteVenueAPIService(eventBriteAPI,
                eventBriteAuthToken,
                eventBriteVenueAPIPath);
    }
}
