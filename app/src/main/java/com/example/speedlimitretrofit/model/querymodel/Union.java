package com.example.speedlimitretrofit.model.querymodel;

import org.simpleframework.xml.Element;

public class Union {

    @Element(name="item")
    private String item;

    @Element(name="recurse")
    private Recurse recurse;

    public Union(String item, Recurse recurse) {
        this.item = "";
        this.recurse = recurse;
    }

}
