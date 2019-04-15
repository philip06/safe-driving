package com.example.speedlimitretrofit.model.querymodel;

import org.simpleframework.xml.Attribute;

public class Recurse {

    @Attribute(name="type")
    private String type;

    public Recurse(String type) {
        this.type = type;
    }

}
