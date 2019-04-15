package com.example.speedlimitretrofit.model.querymodel;

import org.simpleframework.xml.Attribute;

public class BBoxQuery {

    @Attribute(name="e")
    private String east;

    @Attribute(name="n")
    private String north;

    @Attribute(name="s")
    private String south;

    @Attribute(name="w")
    private String west;

    public BBoxQuery(String east, String north, String south, String west) {
        this.east = east;
        this.north = north;
        this.south = south;
        this.west = west;
    }

}
