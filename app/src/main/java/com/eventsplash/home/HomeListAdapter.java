package com.eventsplash.home;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventsplash.R;
import com.eventsplash.databinding.ListItemEventBinding;
import com.eventsplash.eventdetail.handlers.EventWithVenueClickHandler;
import com.eventsplash.eventdetail.models.EventWithVenue;

import java.util.List;

/**
 * Created by jonathanhavstad on 2/25/18.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private EventWithVenueClickHandler.OnSelectEventWithVenue onSelectEventWithVenue;
    private List<EventWithVenue> eventWithVenueList;

    public List<EventWithVenue> getEventWithVenueList() {
        return eventWithVenueList;
    }

    public HomeListAdapter(@NonNull EventWithVenueClickHandler.OnSelectEventWithVenue onSelectEventWithVenue) {
        this.onSelectEventWithVenue = onSelectEventWithVenue;
    }

    public void setEventWithVenueList(List<EventWithVenue> eventWithVenueList) {
        this.eventWithVenueList = eventWithVenueList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemEventBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.list_item_event, parent, false);
        return new ViewHolder(binding.getRoot(), binding, onSelectEventWithVenue);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setEventWithVenue(eventWithVenueList.get(position));
        holder.eventWithVenueClickHandler.setEventWithVenue(eventWithVenueList.get(position));
        holder.binding.setEventClickHandler(holder.eventWithVenueClickHandler);
    }

    @Override
    public int getItemCount() {
        return eventWithVenueList != null ? eventWithVenueList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemEventBinding binding;
        EventWithVenueClickHandler eventWithVenueClickHandler;
        public ViewHolder(View itemView,
                          ListItemEventBinding binding,
                          EventWithVenueClickHandler.OnSelectEventWithVenue onSelectEventWithVenue) {
            super(itemView);
            this.binding = binding;
            this.eventWithVenueClickHandler =
                    new EventWithVenueClickHandler(onSelectEventWithVenue);
        }
    }
}
