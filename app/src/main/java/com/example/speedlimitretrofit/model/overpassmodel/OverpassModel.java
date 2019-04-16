package com.example.speedlimitretrofit.model.overpassmodel;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// model for XML response from Overpass API
// osm is the name of the root tag
// contains a list of nodes and ways
@Root(name="osm", strict=false)
public class OverpassModel {

    @ElementList(name="node", entry="node", inline = true, required=false)
    private List<Node> nodeList;

    @ElementList(name="node", entry="way", inline = true, required=false)
    private List<Way> wayList;

    @Element(name="note", required=false)
    private String note;

    @Element(name="meta", required=false)
    private Meta meta;

    @Attribute(name="version", required=false)
    private String version;

    @Attribute(name="generator", required=false)
    private String generator;

    public OverpassModel() {  }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Way> getWayList() {
        return wayList;
    }

    public void setWayList(List<Way> wayList) {
        this.wayList = wayList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }
}