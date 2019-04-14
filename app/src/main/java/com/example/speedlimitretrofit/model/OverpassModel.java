package com.example.speedlimitretrofit.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// model for XML response from Overpass API
// osm is the name of the root tag
// contains a list of nodes and ways
@Root(name="osm")
public class OverpassModel {

    public List<Node> getNodeList() {
        return nodeList;
    }

    public List<Way> getWayList() {
        return wayList;
    }

    @ElementList(name = "node", inline = true)
    private List<Node> nodeList;

    @ElementList(name = "way", inline = true)
    private List<Way> wayList;

    // model for xml node
    // contains id, latitude and longitude
    // id can be used to reference ways using the nd objects within
    @Root(name = "node")
    private class Node {
        @Attribute(name = "id")
        public String id;

        @Attribute(name = "lat")
        public String lat;

        @Attribute(name = "lon")
        public String lon;

        @ElementList(name = "tag", inline = true)
        public List<Tag> tagList;
    }

    // model for xml way
    // contains list of tag objects and list of nd objects
    // tag contains speed limit data, nd contains a node id
    @Root(name = "way")
    public class Way {
        public List<Tag> getTagList() {
            return tagList;
        }

        public List<ND> getNdList() {
            return ndList;
        }

        @ElementList(name = "tag", inline = true)
        private List<Tag> tagList;

        @ElementList(name = "nd", inline = true)
        private List<ND> ndList;
    }

    // model for xml tag
    // contains speed limit data and other data we won't be using
    @Root(name = "tag")
    public class Tag {
        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Attribute(name = "k")
        private String key;

        @Attribute(name = "v")
        private String value;
    }

    // model for xml nd
    // contains ref which is a node id
    @Root(name = "nd")
    public class ND {
        public String getRef() {
            return ref;
        }

        @Attribute(name = "ref")
        private String ref;
    }
}