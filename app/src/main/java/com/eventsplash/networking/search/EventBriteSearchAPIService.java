package com.eventsplash.networking.search;

import com.eventsplash.model.eventbright.events.SearchResults;
import com.eventsplash.networking.retrofit.EventBriteAPI;
import com.eventsplash.networking.search.exception.EventBriteSearchRequestException;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class EventBriteSearchAPIService {
    private final EventBriteAPI eventBriteAPI;
    private final String eventBriteAuthToken;
    private final String eventBriteSearchApiPath;
    private boolean requestingSearch;

    @Inject
    public EventBriteSearchAPIService(EventBriteAPI eventBriteAPI,
                                      String eventBriteAuthToken,
                                      String eventBriteSearchAPIPath) {
        this.eventBriteAPI = eventBriteAPI;
        this.eventBriteAuthToken = eventBriteAuthToken;
        this.eventBriteSearchApiPath = eventBriteSearchAPIPath;
    }

    public Flowable<SearchResults> searchAll() {
        return eventBriteAPI.searchAll(eventBriteSearchApiPath, eventBriteAuthToken)
            .doOnSubscribe(disposable -> requestingSearch = true)
            .doOnTerminate(() -> requestingSearch = false)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(this::handleError)
            .toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<SearchResults> searchByLatLon(double latitude, double longitude, int radius) {
        String locationLatitude = String.valueOf(latitude);
        String locationLongitude = String.valueOf(longitude);
        StringBuilder within = new StringBuilder();
        within.append(radius);
        within.append("km");
        return eventBriteAPI.searchByLatLon(eventBriteSearchApiPath,
                eventBriteAuthToken,
                locationLatitude,
                locationLongitude,
                within.toString())
                .doOnSubscribe(disposable -> requestingSearch = true)
                .doOnTerminate(() -> requestingSearch = false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::handleError)
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    public boolean isRequestingSearch() {
        return requestingSearch;
    }

    private void handleError(Throwable throwable) {
        throw new EventBriteSearchRequestException();
    }
}
