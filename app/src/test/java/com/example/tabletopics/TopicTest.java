package com.example.tabletopics;

import org.junit.Test;

import static org.junit.Assert.*;

public class TopicTest {

    @Test
    public void getTitle() {
        Category comedy = Category.Comedy;
        Topic topic = new Topic("Test topic", comedy);
        assertEquals("Test topic", topic.getTopic());
    }

    @Test
    public void getIncorrectTitle() {
        Category comedy = Category.Comedy;
        Topic topic = new Topic("Test topic", comedy);
        assertNotEquals("", topic.getTopic());
    }

    @Test
    public void getCategory() {
        Category comedy = Category.Comedy;
        Topic topic = new Topic("Test topic", comedy);
        assertEquals(Category.Comedy, topic.getCategory());
    }

    @Test
    public void getIncorrectCategory() {
        Category comedy = Category.Comedy;
        Topic topic = new Topic("Test topic", comedy);
        assertNotEquals(Category.Technology, topic.getCategory());
    }
}