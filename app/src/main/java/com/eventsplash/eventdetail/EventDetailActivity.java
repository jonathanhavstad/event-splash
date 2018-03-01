package com.eventsplash.eventdetail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

        if (intent != null) {
            Bundle args = intent.getBundleExtra(getString(R.string.event_detail_bundle_key));
            EventDetailBinder eventDetailBinder =
                    (EventDetailBinder) args.getBinder(getString(R.string.event_detail_binder_key));
            if (eventDetailBinder != null) {
                eventWithVenue = eventDetailBinder.getEventWithVenue();
            }
        }

        ActivityDetailBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setEventWithVenue(eventWithVenue);
    }
}
