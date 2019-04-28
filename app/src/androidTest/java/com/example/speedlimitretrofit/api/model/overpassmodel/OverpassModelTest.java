package com.example.speedlimitretrofit.api.model.overpassmodel;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OverpassModelTest {
    //OverpassModel testOPM= new OverpassModel();

    @Test
    public void getNodeList() {
        OverpassModel testOPM= new OverpassModel();

        //new Assert(testOPM.getNodeList(),null);
        List<Node> list = new ArrayList();
        Node temp_Node =new Node();
        temp_Node.setId("0000001");
        temp_Node.setLat("0000001");
        temp_Node.setLon("0000001");
        //list.add(temp_Node);
        list.add(temp_Node);
        testOPM.setNodeList(list);
//        System.out.print(testOPM.getNodeList().size());
       assertEquals(1,testOPM.getNodeList().size());
    }

    @Test
    public void setNodeList() {
        OverpassModel testOPM= new OverpassModel();
        List<Node> list = new ArrayList();
        Node temp_Node =new Node();
        temp_Node.setId("0000001");
        temp_Node.setLat("0000001");
        temp_Node.setLon("0000001");
        //list.add(temp_Node);
        list.add(temp_Node);
        testOPM.setNodeList(list);
//        System.out.print(testOPM.getNodeList().size());
        assertEquals("0000001",testOPM.getNodeList().get(0).getId());
    }


    @Test
    public void getWayList() {
        OverpassModel testOPM= new OverpassModel();
        List<Way> list = new ArrayList();
        Way temp_way =new Way();
        temp_way.setId("01");
        //list.add(temp_Node);
        list.add(temp_way);
        testOPM.setWayList(list);
//        System.out.print(testOPM.getNodeList().size());
        assertEquals("01",testOPM.getWayList().get(0).getId());
    }

    @Test
    public void setWayList() {
        OverpassModel testOPM= new OverpassModel();
        List<Way> list = new ArrayList();
        //Way temp_way =new Way();
        for(int i =0; i<5; i++){
            Way temp_way =new Way();
            temp_way.setId(String.valueOf(i));
            list.add(temp_way);
        }

        testOPM.setWayList(list);
//        System.out.print(testOPM.getNodeList().size());
        assertEquals(5,testOPM.getWayList().size());
    }

    @Test
    public void getNote() {
        OverpassModel testOPM= new OverpassModel();
        testOPM.setNote("testing");
        assertEquals("testing",testOPM.getNote());
    }

    @Test
    public void setNote() {
        OverpassModel testOPM= new OverpassModel();
        testOPM.setNote("testing");
        assertEquals("testing",testOPM.getNote());
    }

    @Test
    public void getMeta() {
        OverpassModel testOPM= new OverpassModel();
        Meta temp_Meta = new Meta();
        temp_Meta.setOsmBase("testing meta");
        testOPM.setMeta(temp_Meta);
        assertEquals("testing meta",testOPM.getMeta().getOsmBase());
    }

    @Test
    public void setMeta() {
        OverpassModel testOPM= new OverpassModel();
        Meta temp_Meta = new Meta();
        temp_Meta.setOsmBase("testing meta");
        testOPM.setMeta(temp_Meta);
        assertEquals("testing meta",testOPM.getMeta().getOsmBase());
    }

    @Test
    public void getVersion() {
        OverpassModel testOPM= new OverpassModel();
        testOPM.setVersion("test");
        assertEquals("test",testOPM.getVersion());
    }

    @Test
    public void setVersion() {
        OverpassModel testOPM= new OverpassModel();
        testOPM.setVersion("test");
        assertEquals("test",testOPM.getVersion());
    }

    @Test
    public void getGenerator() {
        OverpassModel testOPM= new OverpassModel();
        testOPM.setGenerator("testing getGenerator");
        assertEquals("testing getGenerator",testOPM.getGenerator());
    }

    @Test
    public void setGenerator() {
        OverpassModel testOPM= new OverpassModel();
        testOPM.setGenerator("testing getGenerator");
        assertEquals("testing getGenerator",testOPM.getGenerator());
    }
}