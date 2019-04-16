package com.example.speedlimitretrofit.model.overpassmodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// model for xml nd
// contains ref which is a node id
@Root(name="nd")
public class ND {

    @Attribute(name = "ref")
    private String ref;

    public ND() {   }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}