package com.techelevator.services;

import com.techelevator.model.UserDance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    public static class DancePreview {
        private String title;
        private String choreographer;
        private String music;
        private String link;
        private String stepCount;
        private String wallCount;
        private String level;

        public DancePreview(String title, String choreographer, String music, String link,
                            String stepCount, String wallCount, String level) {
            this.title = title;
            this.choreographer = choreographer;
            this.music = music;
            this.link = link;
            this.stepCount = stepCount;
            this.wallCount = wallCount;
            this.level = level;
        }

        public String getTitle() {
            return title;
        }

        public String getChoreographer() {
            return choreographer;
        }

        public String getMusic() {
            return music;
        }

        public String getLink() {
            return link;
        }

        public String getStepCount() {
            return stepCount;
        }

        public String getWallCount() {
            return wallCount;
        }

        public String getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return title + " by " + choreographer + " (" + music + ")";
        }
    }

    public List<DancePreview> searchDances(String query) throws IOException {
        List<DancePreview> results = new ArrayList<>();
        String url = "https://www.copperknob.co.uk/search?Search=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
                + "&Beat=-1&Level=Any&Wall=-1&Lang=Any&SearchType=Any";

        Document doc = Jsoup.connect(url).get();
        Elements dances = doc.select(".listitem");

        for (Element dance : dances) {
            String title = dance.select(".listTitle > a").text();
            String href = dance.select(".listTitle > a").attr("href");
            String link = href.startsWith("http") ? href : "https://www.copperknob.co.uk" + href;
            String choreographer = dance.select(".listTitleColor2").text();

            String info = dance.select(".listInfo > p").text();
            String[] parts = info.split("Music:");
            String beforeMusic = parts[0].trim();
            String music = parts.length > 1 ? parts[1].trim() : "Unknown";

            String[] tokens = beforeMusic.split("\\s+");
            String stepCount = "Unknown";
            String wallCount = "Unknown";
            StringBuilder levelBuilder = new StringBuilder();

            if (tokens.length >= 4) {
                stepCount = tokens[0];  // e.g., "96"
                wallCount = tokens[2];  // e.g., "2"
                for (int i = 4; i < tokens.length; i++) {
                    levelBuilder.append(tokens[i]).append(" ");
                }
            }

            String level = levelBuilder.toString().trim();
            results.add(new DancePreview(title, choreographer, music, link, stepCount, wallCount, level));
        }

        return results;
    }



    public UserDance getDanceDetails(DancePreview preview) throws IOException {
        Document doc = Jsoup.connect(preview.link).get();

        String level = doc.select("div.sheetinfolevel > span").text();
        if (level.isEmpty()) level = "Unknown";

        return new UserDance(preview.title, preview.choreographer, preview.music, level, preview.link);
    }
}
