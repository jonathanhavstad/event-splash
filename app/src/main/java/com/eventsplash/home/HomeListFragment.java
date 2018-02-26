package com.eventsplash.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventsplash.R;
import com.eventsplash.model.eventbright.EventWithVenue;
import com.eventsplash.model.eventbright.events.SearchResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class HomeListFragment extends HomeFragment {
    private HomeListAdapter homeListAdapter;

    public static HomeListFragment newInstance(Context context,
                                               boolean fineLocationPermissionGranted) {
        Bundle args = new Bundle();
        args.putBoolean(context.getString(R.string.fine_location_granted_bundle_arg_key),
                fineLocationPermissionGranted);
        HomeListFragment homeListFragment = new HomeListFragment();
        homeListFragment.setArguments(args);
        return homeListFragment;
    }

    @Override
    protected void updateCurrentPosition(Location location) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_events, container, false);
        RecyclerView eventsListView = view.findViewById(R.id.events_list);
        homeListAdapter = new HomeListAdapter();
        eventsListView.setAdapter(homeListAdapter);
        return view;
    }

    @Override
    public void onSuccess(Object next) {
        if (next instanceof SearchResults) {
            homeViewModel.requestVenueInformationList(((SearchResults) next).getEventList());
        } else if (next instanceof List<?>) {
            List<EventWithVenue> eventWithVenueList = new ArrayList<>();
            for (Object obj : (List<?>) next) {
                if (obj instanceof EventWithVenue) {
                    eventWithVenueList.add((EventWithVenue) obj);
                }
            }
            homeListAdapter.setEventWithVenueList(eventWithVenueList);
        }
    }

    @Override
    public void onFailure() {

    }
}
