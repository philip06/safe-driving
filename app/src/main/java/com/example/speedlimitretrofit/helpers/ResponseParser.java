package com.example.speedlimitretrofit.helpers;

import com.example.speedlimitretrofit.api.model.overpassmodel.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

// Object specifically designed to parse the important information from a OverpassModel
public class ResponseParser {
    private OverpassModel overpassModel;
    private HashMap<String, String> wayIdSpeedHash;
    private HashMap<String, ArrayList<String>> wayNdIdHash;
    private HashMap<String, ArrayList<String>> nodeCoordsHash;

    // Calculates fields upon creation
    public ResponseParser(OverpassModel overpassModel) {
        this.overpassModel = overpassModel;
        this.wayIdSpeedHash = new HashMap<>();
        this.wayNdIdHash = new HashMap<>();
        this.nodeCoordsHash = new HashMap<>();

        calculateNodeHash();
        calculateWayHash();
    }

    // Sets this.nodeCoordsHash = HashMap<id, coords> from the data in the Nodes
    private void calculateNodeHash() {
        // make sure node list isn't empty
        if (this.overpassModel.getNodeList() != null && !this.overpassModel.getNodeList().isEmpty()) {
            for (Node node : this.overpassModel.getNodeList()) {
                // create ArrayList populated with lat and lon coords
                ArrayList<String> coords = new ArrayList<>();
                coords.add(node.getLat());
                coords.add(node.getLon());

                // add to ret: key=id, value=ArrayList(lat, lon)
                this.nodeCoordsHash.put(node.getId(), coords);
            }
        }
    }

    // Sets this.wayNdIdHash = HashMap<wayId, ArrayList<nodeId>> from the data in way.nd
    // Sets this.idSpeedHash = HashMap<nodeId, maxspeed> from the data in way.tag
    private void calculateWayHash() {
        // make sure way list isn't empty
        if (this.overpassModel.getWayList() != null && !this.overpassModel.getWayList().isEmpty()) {
            // iterate through each way in overpassmodel
            for (Way way : this.overpassModel.getWayList()) {
                ArrayList<String> ndIdList = new ArrayList<>();
                String id = way.getId();

                // check for empty list
                if (way.getNdList() != null && !way.getNdList().isEmpty()) {
                    // iterate through nd elements and add their ref attributes to an arraylist
                    for (ND nd : way.getNdList()) {
                        ndIdList.add(nd.getRef());
                    }
                }

                // check for empty list
                if (way.getTagList() != null && !way.getTagList().isEmpty()) {
                    for (Tag t : way.getTagList()) {
                        if (t.getKey().equals("maxspeed")) {
                            this.wayIdSpeedHash.put(id, t.getValue());
                        }
                    }
                }
                this.wayNdIdHash.put(way.getId(), ndIdList);
            }
        }
    }

    // returns the first way id match which contains the given node id
    public String nodeIdToWayId(String nodeId) {
        for (Map.Entry<String, ArrayList<String>> entry : this.wayNdIdHash.entrySet()) {
            String wayId = entry.getKey();
            ArrayList<String> nodeIds = entry.getValue();
            if (nodeIds.contains(nodeId)) {
                return wayId;
            }
        }

        return null;
    }

    public ArrayList<String> getClosestNode(double userLat, double userLon, String measurementUnit) {
        // initialize distanceCalculator outside loop to save memory
        DistanceCalculator distanceCalculator;
        // ArrayList holding the node closest to the user currently
        // initialize a 3-length tuple to allow usage of .set()
        ArrayList<String> closestNode = new ArrayList<>();

        double lowestDist = 1000000000.0;

        if (!nodeCoordsHash.isEmpty() && !this.wayIdSpeedHash.isEmpty()) {
            // iterate through nodeCoordsHash
            // calculates closest node to (userLat, userLon) which has speed limit data
            for (Map.Entry<String, ArrayList<String>> entry : nodeCoordsHash.entrySet()) {
                String nodeId = entry.getKey();
                String wayId = this.nodeIdToWayId(nodeId);
                String speedValue = this.wayIdSpeedHash.get(wayId);

                // if id has no speedlimit data, skip
                if (speedValue == null) {
                    continue;
                }
                ArrayList<String> coords = entry.getValue();
                double nodeLat = Double.parseDouble(coords.get(0));
                double nodeLon = Double.parseDouble(coords.get(1));
                distanceCalculator = new DistanceCalculator();
                double dist = distanceCalculator.distance(nodeLat, nodeLon, userLat, userLon, measurementUnit);
                if (dist < lowestDist) {
                    // allow arraylist to be used like a tuple
                    if (closestNode.isEmpty()) {
                        closestNode.add("");
                        closestNode.add("");
                        closestNode.add("");
                    }
                    lowestDist = dist;
                    closestNode.set(0, nodeId);
                    closestNode.set(1, String.valueOf(dist));
                    closestNode.set(2, speedValue.split(" ")[0]);
                }
            }
        }

        return closestNode;
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

    public HashMap<String, String> getWayIdSpeedHash() {
        return this.wayIdSpeedHash;
    }
}
