package com.example.speedlimitretrofit.api.model.overpassmodel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// model for xml nd
// contains ref which is a node id
@Root(name="nd", strict=false)
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
