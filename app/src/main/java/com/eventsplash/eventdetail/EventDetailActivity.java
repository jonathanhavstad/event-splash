package com.eventsplash.eventdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.eventsplash.R;
import com.eventsplash.databinding.ActivityDetailBinding;
import com.eventsplash.eventdetail.models.EventWithVenue;

/**
 * Created by jonathanhavstad on 2/28/18.
 */

public class EventDetailActivity extends AppCompatActivity {
    private EventWithVenue eventWithVenue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra(getString(R.string.event_detail_bundle_key));
        EventDetailBinder eventDetailBinder =
                (EventDetailBinder) args.getBinder(getString(R.string.event_detail_binder_key));
        if (eventDetailBinder != null) {
            eventWithVenue = eventDetailBinder.getEventWithVenue();
        }

        ActivityDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setEventWithVenue(eventWithVenue);

        Toolbar toolbar = binding.getRoot().findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(eventWithVenue.getEvent().getName().getText());
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
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
