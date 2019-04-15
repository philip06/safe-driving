package com.example.speedlimitretrofit.model.overpassmodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

// model for xml node
// contains id, latitude and longitude
// id can be used to reference ways using the nd objects within
@Root(name="node")
public class Node {

    @Attribute(name = "id", required=false)
    private String id;

    @Attribute(name = "lat", required=false)
    private String lat;

    @Attribute(name = "lon", required=false)
    private String lon;

    @ElementList(entry="tag", name="tag", required=false)
    private List<Tag> tagList;

    public Node() {   }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }
}
