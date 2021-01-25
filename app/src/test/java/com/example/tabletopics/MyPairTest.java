package com.example.tabletopics;

import org.junit.Test;
import static org.junit.Assert.*;

public class MyPairTest {

    @Test
    public void getName() {
        MyPair p = new MyPair("Tester", "10");
        assertEquals("Tester", p.getName());
    }

    @Test
    public void getCount() {
        MyPair p = new MyPair("Tester", "10");
        assertEquals("10", p.getCount());
    }

    @Test
    public void testToString() {
        MyPair p = new MyPair("Tester", "10");
        assertEquals((String.format( "%7.15s %7s\n\n", p.getName(), p.getCount() )), p.toString());
    }
}