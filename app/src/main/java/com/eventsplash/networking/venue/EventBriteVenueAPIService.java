package com.eventsplash.networking.venue;

import android.support.annotation.NonNull;

import com.eventsplash.model.eventbright.venues.Venue;
import com.eventsplash.networking.retrofit.EventBriteAPI;
import com.eventsplash.networking.venue.exception.EventBriteVenueRequestException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class EventBriteVenueAPIService {
    private final EventBriteAPI eventBriteAPI;
    private final String eventBriteAuthToken;
    private final String eventBrightVenueAPIPath;
    private boolean requestingVenueInformation;

    public EventBriteVenueAPIService(EventBriteAPI eventBriteAPI, String eventBriteAuthToken, String eventBrightVenueAPIPath) {
        this.eventBriteAPI = eventBriteAPI;
        this.eventBriteAuthToken = eventBriteAuthToken;
        this.eventBrightVenueAPIPath = eventBrightVenueAPIPath;
    }

    public Flowable<Venue> getVenueInformation(@NonNull String venueID) {
        return eventBriteAPI.getVenue(eventBrightVenueAPIPath, venueID, eventBriteAuthToken)
                .doOnSubscribe(disposable -> requestingVenueInformation = true)
                .doOnTerminate(() -> requestingVenueInformation = false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::handleError)
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    public boolean isRequestingVenueInformation() {
        return requestingVenueInformation;
    }

    private void handleError(Throwable throwable) {
        throw new EventBriteVenueRequestException();
    }
}
