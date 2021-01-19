package com.example.tabletopics;

public class MyPair
{
    private final String name;
    private final String count;

    public MyPair(String aName, String aCount)
    {
        name   = aName;
        count = aCount;
    }

    public String getName()   { return name; }
    public String getCount() { return count; }

    public String toString() {
        return String.format( "%7.15s %7s\n\n", name, count );
    }
}