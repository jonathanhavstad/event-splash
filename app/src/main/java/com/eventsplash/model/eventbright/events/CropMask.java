
package com.eventsplash.model.eventbright.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CropMask {

    @SerializedName("top_left")
    @Expose
    private TopLeft topLeft;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;

    public TopLeft getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(TopLeft topLeft) {
        this.topLeft = topLeft;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}
