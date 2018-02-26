
package com.eventsplash.model.eventbright.venues;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("address_1")
    @Expose
    private String address1;
    @SerializedName("address_2")
    @Expose
    private Object address2;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("localized_address_display")
    @Expose
    private String localizedAddressDisplay;
    @SerializedName("localized_area_display")
    @Expose
    private String localizedAreaDisplay;
    @SerializedName("localized_multi_line_address_display")
    @Expose
    private List<String> localizedMultiLineAddressDisplay = null;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public Object getAddress2() {
        return address2;
    }

    public void setAddress2(Object address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocalizedAddressDisplay() {
        return localizedAddressDisplay;
    }

    public void setLocalizedAddressDisplay(String localizedAddressDisplay) {
        this.localizedAddressDisplay = localizedAddressDisplay;
    }

    public String getLocalizedAreaDisplay() {
        return localizedAreaDisplay;
    }

    public void setLocalizedAreaDisplay(String localizedAreaDisplay) {
        this.localizedAreaDisplay = localizedAreaDisplay;
    }

    public List<String> getLocalizedMultiLineAddressDisplay() {
        return localizedMultiLineAddressDisplay;
    }

    public void setLocalizedMultiLineAddressDisplay(List<String> localizedMultiLineAddressDisplay) {
        this.localizedMultiLineAddressDisplay = localizedMultiLineAddressDisplay;
    }

}
