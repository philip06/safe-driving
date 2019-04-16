package com.example.speedlimitretrofit.controller;

import com.example.speedlimitretrofit.model.overpassmodel.*;

import java.util.HashMap;
import java.util.ArrayList;

public class ResponseParser {
    private OverpassModel overpassModel;
    private HashMap<String, String> nodeIdSpeedHash;
    private HashMap<String, ArrayList<String>> wayNdIdHash;
    private HashMap<String, ArrayList<String>> nodeCoordsHash;

    // Calculates fields upon creation
    public ResponseParser(OverpassModel overpassModel) {
        this.overpassModel = overpassModel;
        this.nodeIdSpeedHash = new HashMap<>();
        this.wayNdIdHash = new HashMap<>();
        this.nodeCoordsHash = new HashMap<>();

        calculateNodeHash();
        calculateWayHash();
    }

    // Sets this.nodeCoordsHash = HashMap<id, coords> from the data in the Nodes
    private void calculateNodeHash() {

        for (Node node: this.overpassModel.getNodeList()) {
            // create ArrayList populated with lat and lon coords
            ArrayList<String> coords = new ArrayList<>();
            coords.add(node.getLat());
            coords.add(node.getLon());

            // add to ret: key=id, value=ArrayList(lat, lon)
            this.nodeCoordsHash.put(node.getId(), coords);
        }

    }

    // Sets this.wayNdIdHash = HashMap<wayId, ArrayList<nodeId>> from the data in way.nd
    // Sets this.idSpeedHash = HashMap<nodeId, maxspeed> from the data in way.tag
    private void calculateWayHash() {

        // iterate through each way in overpassmodel
        for (Way way: this.overpassModel.getWayList()) {
            ArrayList<String> ndIdList = new ArrayList<>();
            String id = way.getId();
            // iterate through nd elements and add their ref attributes to an arraylist
            for (ND nd: way.getNdList()) {
                ndIdList.add(nd.getRef());
            }

            for (Tag t: way.getTagList()) {
                if (t.getKey().equals("maxspeed")) {
                    this.nodeIdSpeedHash.put(id, t.getValue());
                }
            }
            this.wayNdIdHash.put(way.getId(), ndIdList);
        }

    }

    public OverpassModel getOverpassModel() {
        return this.overpassModel;
    }

    public HashMap<String, ArrayList<String>> getWayNdIdHash() {
        return this.wayNdIdHash;
    }

    public HashMap<String, ArrayList<String>> getNodeCoordsHash() {
        return this.nodeCoordsHash;
    }

    public HashMap<String, String> getNodeIdSpeedHash() {
        return this.nodeIdSpeedHash;
    }
}
