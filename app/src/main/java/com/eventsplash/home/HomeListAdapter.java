package com.eventsplash.home;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eventsplash.R;
import com.eventsplash.databinding.ListItemEventBinding;
import com.eventsplash.model.eventbright.EventWithVenue;

import java.util.List;

/**
 * Created by jonathanhavstad on 2/25/18.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    private List<EventWithVenue> eventWithVenueList;

    public List<EventWithVenue> getEventWithVenueList() {
        return eventWithVenueList;
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
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setEventWithVenue(eventWithVenueList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventWithVenueList != null ? eventWithVenueList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ListItemEventBinding binding;
        public ViewHolder(View itemView,
                          ListItemEventBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }
}
