package com.eventsplash.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.eventsplash.base.Lifecycle;
import com.eventsplash.model.eventbright.events.Event;
import com.eventsplash.networking.EventBriteRequestManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class HomeViewModel implements HomeContract.ViewModel {
    private AsyncProcessor<Object> eventBriteRequestProcessor;
    private EventBriteRequestManager eventBriteRequestManager;
    private Disposable eventBriteRequestDisposable;

    private HomeContract.View viewCallback;

    public HomeViewModel(String eventBriteBaseURL,
                         String eventBriteAuthToken,
                         String eventBriteSearchAPIPath,
                         String eventBriteVenueAPIPath) {
        this.eventBriteRequestManager = new EventBriteRequestManager(eventBriteBaseURL,
                eventBriteAuthToken,
                eventBriteSearchAPIPath,
                eventBriteVenueAPIPath);
    }

    @Override
    public void onViewResumed() {
        if (isNetworkRequestMade()) {
            eventBriteRequestProcessor.subscribe(createHomeSubscriber());
        }
    }

    @Override
    public void onViewAttached(@NonNull Lifecycle.View viewCallback) {
        this.viewCallback = (HomeContract.View) viewCallback;
    }

    @Override
    public void onViewDetached() {
        this.viewCallback = null;

        if (isNetworkRequestMade()) {
            eventBriteRequestDisposable.dispose();
        }
    }

    @Override
    public void requestSearch() {
        eventBriteRequestProcessor = AsyncProcessor.create();
        eventBriteRequestDisposable = eventBriteRequestProcessor
                .subscribeWith(createHomeSubscriber());
        eventBriteRequestManager.requestSearch().subscribe(eventBriteRequestProcessor);
    }

    @Override
    public void requestSearchByLatLon(double latitude,
                                      double longitude,
                                      int radius) {
        eventBriteRequestProcessor = AsyncProcessor.create();
        eventBriteRequestDisposable = eventBriteRequestProcessor
                .subscribeWith(createHomeSubscriber());
        eventBriteRequestManager.requestSearchByLatLon(latitude, longitude, radius)
                .subscribe(eventBriteRequestProcessor);
    }

    @Override
    public void requestVenueInformation(String venueID) {
        eventBriteRequestProcessor = AsyncProcessor.create();
        eventBriteRequestDisposable = eventBriteRequestProcessor
                .subscribeWith(createHomeSubscriber());
        eventBriteRequestManager.requestVenueInformation(venueID)
                .subscribe(eventBriteRequestProcessor);
    }

    @Override
    public void requestVenueInformationList(List<Event> eventList) {
        Map<String, Event> venueIDToEventMap = new HashMap<>();
        for (Event event : eventList) {
            venueIDToEventMap.put(event.getVenueId(), event);
        }

        eventBriteRequestProcessor = AsyncProcessor.create();
        eventBriteRequestDisposable = eventBriteRequestProcessor
                .subscribeWith(createHomeSubscriber());
        eventBriteRequestManager.requestVenueInformationList(venueIDToEventMap)
                .subscribe(eventBriteRequestProcessor);
    }

    private HomeSubscriber createHomeSubscriber() {
        return new HomeSubscriber(o -> { if (viewCallback != null) viewCallback.onSuccess(o); },
                t -> { if (viewCallback != null) viewCallback.onFailure(); },
                () -> {});
    }

    private boolean isNetworkRequestMade() {
        return eventBriteRequestDisposable != null;
    }

    private interface OnNext {
        void call(Object o);
    }

    private interface OnError {
        void call(Throwable t);
    }

    private interface OnComplete {
        void call();
    }

    private static class HomeSubscriber extends DisposableSubscriber<Object> {
        final OnNext onNext;
        final OnError onError;
        final OnComplete onComplete;

        public HomeSubscriber(@NonNull OnNext onNext,
                              @NonNull OnError onError,
                              @NonNull OnComplete onComplete) {
            this.onNext = onNext;
            this.onError = onError;
            this.onComplete = onComplete;
        }

        @Override
        public void onNext(Object o) {
            onNext.call(o);
        }

        @Override
        public void onError(Throwable t) {
            onError.call(t);
        }

        @Override
        public void onComplete() {
            onComplete.call();
        }
    }
}
