package com.eventsplash.home;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.eventsplash.R;
import com.eventsplash.base.BaseFragment;
import com.eventsplash.base.Lifecycle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public abstract class HomeFragment extends BaseFragment implements HomeContract.View {
    protected HomeViewModel homeViewModel;
    protected int defaultSearchRadius;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        boolean fineLocationPermissionGranted;
        if (args != null) {
            fineLocationPermissionGranted =
                    args.getBoolean(getString(R.string.fine_location_granted_bundle_arg_key));
        } else {
            fineLocationPermissionGranted = false;
        }
        homeViewModel = new HomeViewModel(getActivity());
        if (fineLocationPermissionGranted) {
            try {
                defaultSearchRadius = getActivity().getResources().getInteger(R.integer.default_search_radius);
                requestLocation();
            } catch (SecurityException se) {

            }
        } else {
            homeViewModel.requestSearch();
        }
    }

    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return homeViewModel;
    }

    private void requestLocation() throws SecurityException {
        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(getActivity());

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        homeViewModel.requestSearchByLatLon(location.getLatitude(),
                                location.getLongitude(),
                                defaultSearchRadius);
                        updateCurrentPosition(location);
                    }
                });
    }

    protected abstract void updateCurrentPosition(Location location);
}
