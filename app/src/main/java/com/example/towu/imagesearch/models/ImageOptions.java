package com.example.towu.imagesearch.models;

import java.io.Serializable;

/**
 * Created by towu on 11/2/15.
 */
public class ImageOptions implements Serializable{
    public String size;
    public String color;
    public String type;
    public String site;

    public ImageOptions() {
        size = "";
        color = "";
        type = "";
        site = "";
    }


}
