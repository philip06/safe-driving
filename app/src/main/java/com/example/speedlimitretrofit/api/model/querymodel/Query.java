package com.example.speedlimitretrofit.api.model.querymodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Query {

    @Attribute(name="type", required=false)
    private String type;

    @Element(name="has-kv")
    private HasKV has_kv;

    @Element(name="around")
    private Around around;

    public Query(String type, HasKV hasKV, Around around) {
        this.type = type;
        this.has_kv = hasKV;
        this.around = around;
    }
}