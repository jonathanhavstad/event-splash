package com.eventsplash.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.eventsplash.R;
import com.eventsplash.login.FingerprintLoginActivity;
import com.eventsplash.eventdetail.models.EventWithVenue;
import com.eventsplash.model.eventbright.events.SearchResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class HomeListFragment extends HomeFragment {
    private static int LOGIN_REQUEST = 2000;

    private HomeListAdapter homeListAdapter;
    private Button loginButton;
    private View.OnClickListener loginListener;
    private View.OnClickListener logoutListener;

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
        homeListAdapter = new HomeListAdapter(this::launchEventDetailFragment);
        eventsListView.setAdapter(homeListAdapter);
        loginButton = view.findViewById(R.id.login_button);
        loginListener = v -> {
            Intent loginActivity = new Intent(getContext(), FingerprintLoginActivity.class);
            startActivityForResult(loginActivity, LOGIN_REQUEST);
        };
        logoutListener = v -> {
            logout();
        };
        if (isUserLoggedIn()) {
            applyLogin();
        } else {
            applyLogout();
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String username = data.getStringExtra(getString(R.string.username_extra_key));
                    login(username);
                }
            }
        }
    }

    private void login(String username) {
        getContext()
                .getSharedPreferences(getString(R.string.login_status_pref_name), 0)
                .edit()
                .putBoolean(getString(R.string.logged_in_pref_name), true)
                .apply();
        getContext()
                .getSharedPreferences(getString(R.string.logged_in_username_pref_name), 0)
                .edit()
                .putString(getString(R.string.username_pref_name), username)
                .apply();
        applyLogin();
    }

    private void applyLogin() {
        loginButton.setOnClickListener(logoutListener);
        loginButton.setText(R.string.logout_text);
    }

    private void logout() {
        getContext()
                .getSharedPreferences(getString(R.string.login_status_pref_name), 0)
                .edit()
                .putBoolean(getString(R.string.logged_in_pref_name), false)
                .apply();
        getContext()
                .getSharedPreferences(getString(R.string.logged_in_username_pref_name), 0)
                .edit()
                .putString(getString(R.string.username_pref_name), null)
                .apply();
        applyLogout();
    }

    private void applyLogout() {
        loginButton.setOnClickListener(loginListener);
        loginButton.setText(R.string.login_text);
    }

    private boolean isUserLoggedIn() {
        return getContext()
                .getSharedPreferences(getString(R.string.login_status_pref_name), 0)
                .getBoolean(getString(R.string.logged_in_pref_name), false);
    }
}
