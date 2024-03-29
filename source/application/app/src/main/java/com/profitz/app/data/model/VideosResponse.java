package com.profitz.app.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("results")
    @Expose
    private final List<Video> videos = null;

    public int getId() {
        return id;
    }

    public List<Video> getVideos() {
        return videos;
    }
}
