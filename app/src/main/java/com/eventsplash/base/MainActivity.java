package com.eventsplash.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eventsplash.R;
import com.eventsplash.eventdetail.EventDetailActivity;
import com.eventsplash.eventdetail.EventDetailBinder;
import com.eventsplash.eventdetail.models.EventWithVenue;
import com.eventsplash.home.HomeFragment;
import com.eventsplash.home.HomeListFragment;
import com.eventsplash.home.HomePageFragment;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnLaunchEventDetail {
    private static final int REQUEST_ACCESS_FINE_LOCATION = 1001;

    private boolean fineLocationPermissionGranted;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }};

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            checkForLocationPermission();
        } else {
            fineLocationPermissionGranted = true;
            loadHomeFragments();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fineLocationPermissionGranted = true;
                } else {
                    fineLocationPermissionGranted = false;
                }
                loadHomeFragments();
                break;
            }
            default:
        }
    }

    private void checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                View mainContentView = findViewById(R.id.main_content);
                Snackbar.make(mainContentView, "Allow access to GPS", Snackbar.LENGTH_LONG)
                        .setAction("ACCEPT", v -> {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_ACCESS_FINE_LOCATION);
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_ACCESS_FINE_LOCATION);

            }
        } else {
            fineLocationPermissionGranted = true;
            loadHomeFragments();
        }
    }

    private void loadHomeFragments() {
        HomeListFragment homeListFragment =
                HomeListFragment.newInstance(this, fineLocationPermissionGranted);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.events_content, homeListFragment)
                .commit();

        HomePageFragment homePageFragment =
                HomePageFragment.newInstance(this, fineLocationPermissionGranted);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, homePageFragment)
                .commit();
    }

    @Override
    public void launchEventDetail(EventWithVenue eventWithVenue) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        Bundle args = new Bundle();
        args.putBinder(getString(R.string.event_detail_binder_key),
                new EventDetailBinder(eventWithVenue));
        intent.putExtra(getString(R.string.event_detail_bundle_key), args);
        startActivity(intent);
    }
}
