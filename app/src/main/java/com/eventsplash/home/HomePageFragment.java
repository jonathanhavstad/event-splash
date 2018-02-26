package com.eventsplash.home;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventsplash.R;
import com.eventsplash.model.eventbright.EventWithVenue;
import com.eventsplash.model.eventbright.events.SearchResults;
import com.eventsplash.model.eventbright.venues.Venue;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class HomePageFragment extends HomeFragment
        implements HomeContract.View,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener {
    private GoogleMap map;
    private Location currentLocation;
    private boolean updatedMapPoint;

    public static HomePageFragment newInstance(Context context,
            boolean fineLocationPermissionGranted) {
        Bundle args = new Bundle();
        args.putBoolean(context.getString(R.string.fine_location_granted_bundle_arg_key),
                fineLocationPermissionGranted);
        HomePageFragment homePageFragment = new HomePageFragment();
        homePageFragment.setArguments(args);
        return homePageFragment;
    }

    @Override
    protected void updateCurrentPosition(Location location) {
        currentLocation = location;
        if (map != null && !updatedMapPoint) {
            updateMapPoint(location);
        }
    }

    private void updateMapPoint(Location location) {
        LatLng gMapPoint = new LatLng(location.getLatitude(),
                location.getLongitude());
        CameraPosition lastPos =
                new CameraPosition.Builder().target(gMapPoint).zoom(10).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(lastPos));
        updatedMapPoint = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction()
                .add(R.id.map_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onSuccess(Object next) {
        if (next instanceof SearchResults) {
            homeViewModel.requestVenueInformationList(((SearchResults) next).getEventList());
        } else if (next instanceof Venue) {
            addMarker((Venue) next);
        } else if (next instanceof List<?>) {
            for (Object obj : (List<?>) next) {
                if (obj instanceof Venue) {
                    addMarker((Venue) obj);
                } else if (obj instanceof EventWithVenue) {
                    addMarker((EventWithVenue) obj);
                }
            }
        }
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(this);
        if (currentLocation != null && !updatedMapPoint) {
            updateMapPoint(currentLocation);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private void addMarker(Venue venue) {
        String latitudeSrc = venue.getLatitude();
        String longitudeSrc = venue.getLongitude();
        try {
            double latitude = Double.valueOf(latitudeSrc);
            double longitude = Double.valueOf(longitudeSrc);
            addMarker(latitude, longitude, "", venue.getName());
        } catch (NumberFormatException nfe) {

        }
    }

    private void addMarker(EventWithVenue eventWithVenue) {
        String latitudeSrc = eventWithVenue.getVenue().getLatitude();
        String longitudeSrc = eventWithVenue.getVenue().getLongitude();
        try {
            double latitude = Double.valueOf(latitudeSrc);
            double longitude = Double.valueOf(longitudeSrc);
            addMarker(latitude,
                    longitude,
                    eventWithVenue.getEvent().getName().getText(),
                    eventWithVenue.getVenue().getName());
        } catch (NumberFormatException nfe) {

        }
    }

    private void addMarker(double latitude,
                           double longitude,
                           String eventName,
                           String venueName) {
        if (map != null) {
            LatLng eventPoint = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions()
                    .position(eventPoint)
                    .title(eventName)
                    .snippet(venueName));
        }
    }
}
