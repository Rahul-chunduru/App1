package com.example.mycseapp;

/**
 * Created by sri on 10/8/17.
 */
/////////Class representing a single chat message
public class Item {
    //////// name of the poster, message in the chat
    private String name ;
    private String message ;
    ///////// pos to tell whether it is you who sent it or not
    public  boolean pos  ;
    public Item(String n, String m)
    {
        super() ;
        this.name = n ;
        this.message = m ;
    }
    //////for accessing the fields
    public String getName()
    {
        return this.name ;
    }
    public String getMessage()
    {
        return this.message ;
    }
}
