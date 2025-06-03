package com.techelevator.Model;

public class UserDance {
    private String title;
    private String choreographer;
    private String musicInfo;
    private String level;
    private String url;
    private boolean learned;

    public UserDance(String title, String choreographer, String musicInfo, String level, String url) {
        this.title = title;
        this.choreographer = choreographer;
        this.musicInfo = musicInfo;
        this.level = level;
        this.url = url;
        this.learned = false;
    }
    public boolean isLearned() {
        return learned;
    }

    public void setLearned(boolean learned) {
        this.learned = learned;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChoreographer() {
        return choreographer;
    }

    public void setChoreographer(String choreographer) {
        this.choreographer = choreographer;
    }

    public String getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(String musicInfo) {
        this.musicInfo = musicInfo;
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

    @Override
    public String toString() {
        return "Title: " + title + "\n" +
                "Choreographer: " + choreographer + "\n" +
                "Music: " + musicInfo + "\n" +
                "Level: " + level + "\n" +
                "Learned?: " + (learned ? "Yes" : "No") + "\n" +
                "URL: " + url;
    }

}
