package com.example.speedlimitretrofit.model.querymodel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

// model used to create the queries
@Root(name="osm-script")
@Order(elements={"query", "union", "print"})
public class QueryModel {

    public Query getQuery() {
        return query;
    }

    public Union getUnion() {
        return union;
    }

    public String getPrint() {
        return print;
    }

    @Element(name="query")
    private Query query;

    @Element(name="union")
    private Union union;

    @Element(name="print")
    private String print;

    public QueryModel(Query query, Union union, String print) {
        this.query = query;
        this.union = union;
        this.print = print;
    }

}



