package com.example.speedlimitretrofit.model.overpassmodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

// model for xml way
// contains list of tag objects and list of nd objects
// tag contains speed limit data, nd contains a node id
@Root(name="way", strict=false)
public class Way {

    @ElementList(entry="tag", inline = true, required=false)
    private List<Tag> tagList;

    @ElementList(entry="nd", inline = true, required=false)
    private List<ND> ndList;

    @Attribute(name="id", required=false)
    private String id;

    public Way() {    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public List<ND> getNdList() {
        return ndList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public void setNdList(List<ND> ndList) {
        this.ndList = ndList;
    }
}