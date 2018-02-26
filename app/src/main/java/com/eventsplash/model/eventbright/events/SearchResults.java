package com.eventsplash.model.eventbright.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jonathanhavstad on 2/24/18.
 */

public class SearchResults {
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    @SerializedName("events")
    @Expose
    private List<Event> eventList;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
