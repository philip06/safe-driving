package com.example.speedlimitretrofit.model.querymodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Query {

    @Attribute(name="type", required=false)
    private String type;

    @Element(name="has-kv")
    private HasKV has_kv;

    @Element(name="bbox-query")
    private BBoxQuery bbox_query;

    public Query(String type, HasKV hasKV, BBoxQuery bBoxQuery) {
        this.type = type;
        this.has_kv = hasKV;
        this.bbox_query = bBoxQuery;
    }
}