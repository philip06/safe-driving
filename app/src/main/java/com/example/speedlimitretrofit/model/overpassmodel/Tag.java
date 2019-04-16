package com.example.speedlimitretrofit.model.overpassmodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

// model for xml tag
// contains speed limit data and other data we won't be using
@Root(name="tag", strict=false)
public class Tag {

    @Attribute(name = "k", required=false)
    private String key;

    @Attribute(name = "v", required=false)
    private String value;

    public Tag() {    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
