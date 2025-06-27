package com.techelevator.model;

public class Dance {
    private int danceId;
    private int userId;
    private boolean isLearned;
    private String danceName;
    private String songName;
    private String artistName;
    private int count;
    private int walls;
    private String level;
    private String copperknobLink;
    private String demoUrl;
    private String tutorialUrl;

    public Dance() {
    }

    public Dance(int danceId, int userId, boolean isLearned, String danceName, String songName, String artistName, int count, int walls, String level, String copperknobLink, String demoUrl, String tutorialUrl) {
        this.danceId = danceId;
        this.userId = userId;
        this.isLearned = isLearned;
        this.danceName = danceName;
        this.songName = songName;
        this.artistName = artistName;
        this.count = count;
        this.walls = walls;
        this.level = level;
        this.copperknobLink = copperknobLink;
        this.demoUrl = demoUrl;
        this.tutorialUrl = tutorialUrl;
    }

    public int getDanceId() {
        return danceId;
    }

    public void setDanceId(int danceId) {
        this.danceId = danceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public String getDanceName() {
        return danceName;
    }

    public void setDanceName(String danceName) {
        this.danceName = danceName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getWalls() {
        return walls;
    }

    public void setWalls(int walls) {
        this.walls = walls;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCopperknobLink() {
        return copperknobLink;
    }

    public void setCopperknobLink(String copperknobLink) {
        this.copperknobLink = copperknobLink;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    public String getTutorialUrl() {
        return tutorialUrl;
    }

    public void setTutorialUrl(String tutorialUrl) {
        this.tutorialUrl = tutorialUrl;
    }
}
