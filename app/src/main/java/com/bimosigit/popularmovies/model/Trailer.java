package com.bimosigit.popularmovies.model;

/**
 * Created by sigitbn on 7/30/17.
 */

public class Trailer {

    private String key;
    private String site;

    private String thumnailUrl;

    public static final String TRAILER_KEY = "key";
    public static final String TRAILER_SITE = "site";

    public Trailer() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getThumnailUrl() {
        thumnailUrl = "https://img.youtube.com/vi/" + key + "/0.jpg";

        return thumnailUrl;
    }

}
