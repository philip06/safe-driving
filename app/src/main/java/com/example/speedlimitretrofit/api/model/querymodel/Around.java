package com.example.speedlimitretrofit.api.model.querymodel;

import org.simpleframework.xml.Attribute;

public class Around {

    @Attribute(name="radius")
    private String radius;

    @Attribute(name="lat")
    private double lat;

    @Attribute(name="lon")
    private double lon;

    public Around(String radius, double lat, double lon) {
        this.radius = radius;
        this.lat = lat;
        this.lon = lon;
    }
}