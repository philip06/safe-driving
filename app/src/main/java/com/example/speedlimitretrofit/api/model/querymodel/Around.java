package com.example.speedlimitretrofit.api.model.querymodel;

import org.simpleframework.xml.Attribute;

public class Around {

    @Attribute(name="radius")
    private String radius;

    @Attribute(name="lat")
    private String lat;

    @Attribute(name="lon")
    private String lon;

    public Around(String radius, String lat, String lon) {
        this.radius = radius;
        this.lat = lat;
        this.lon = lon;
    }
}