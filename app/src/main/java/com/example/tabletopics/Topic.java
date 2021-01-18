package com.example.tabletopics;

import java.util.Locale;

public class Topic {
    public String title;
    public Category category;
    private enum Category{
        Sport,
        Comedy,
        Technology,
        Current_Affairs,
        History,
        Contests,
        Love,
        Fantasy,
        Travel
    }
    public double rating;

    public Topic(String title, Category category){
        this.category = category;
        this.title = title;
    }
}
