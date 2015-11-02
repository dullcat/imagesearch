package com.example.towu.imagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by towu on 11/1/15.
 */
public class ImageResult implements Serializable{
    public String url;
    public String tbUrl;
    public String title;

    public ImageResult(JSONObject json){
        try {
            this.url = json.getString("url");
            this.tbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJsonArray(JSONArray array){
        ArrayList<ImageResult> results = new ArrayList<>();
        for (int i=0; i<array.length();i++){
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
