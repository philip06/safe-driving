package com.example.speedlimitretrofit.api.model.querymodel;

import org.simpleframework.xml.Attribute;

public class HasKV {

    @Attribute(name="k")
    private String k;

    public HasKV(String k) {
        this.k = k;
    }
}
