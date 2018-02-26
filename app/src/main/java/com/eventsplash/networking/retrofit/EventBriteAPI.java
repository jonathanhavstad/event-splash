package com.eventsplash.networking.retrofit;

import com.eventsplash.model.eventbright.events.SearchResults;
import com.eventsplash.model.eventbright.venues.Venue;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public interface EventBriteAPI {
    @GET("{search}")
    Observable<SearchResults> searchAll(@Path("search") String search,
                                      @Query("token") String token);

    @GET("{search}")
    Observable<SearchResults> searchByLatLon(@Path("search") String search,
                                             @Query("token") String token,
                                             @Query("location.latitude") String latitude,
                                             @Query("location.longitude") String longitude,
                                             @Query("location.within") String within);

    @GET("{venue}/{venueID}")
    Observable<Venue> getVenue(@Path("venue") String venue,
                               @Path("venueID") String venueId,
                               @Query("token") String token);
}
