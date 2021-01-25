package com.example.tabletopics;

enum Category{
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

public class Topic {
    public String title;
    public Category category;

    public double rating;

    public String getTopic(){
        return title;
    }

    public Category getCategory(){
        return category;
    }

    public Topic(String title, Category category){
        this.category = category;
        this.title = title;
    }
}
