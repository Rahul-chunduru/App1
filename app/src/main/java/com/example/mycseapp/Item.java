package com.example.mycseapp;


/**
 * Class represents a single chat message
 */

public class Item {
    /**  name of the poster, message in the chat , pos tells whether it is you who sent it or not */
    private String name ;
    private String message ;

    public  boolean pos  ;
    public Item(String n, String m)
    {
        super() ;
        this.name = n ;
        this.message = m ;
    }
    ///for accessing the fields
    public String getName()
    {
        return this.name ;
    }
    public String getMessage()
    {
        return this.message ;
    }
}
