package com.example.speedlimitretrofit.model.overpassmodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="meta", strict=false)
public class Meta {
    @Attribute(name="osm_base", required=false)
    private String osmBase;

    public Meta() { }

    public String getOsmBase() {
        return osmBase;
    }

    public void setOsmBase(String osmBase) {
        this.osmBase = osmBase;
    }
}
