package com.techelevator.model;

public class DanceSearch {
    private String title;
    private String artist;
    private int count;
    private int wall;
    private String level;
    private String url;

    public DanceSearch() {
    }

    public DanceSearch(String title, String artist, int count, int wall, String level, String url) {
        this.title = title;
        this.artist = artist;
        this.count = count;
        this.wall = wall;
        this.level = level;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
