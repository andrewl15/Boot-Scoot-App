package com.techelevator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CopperknobScrapper {
    public static void main(String[] args) {
        String url = "https://www.copperknob.co.uk/";
        try {
            Document document = Jsoup.connect(url).get();
            Elements dances = document.select(".listitem");

            for (Element dance : dances) {
                // Grab dance title
                String title = dance.select(".listTitle > a").text();

                // Grab choreographer names
                String choreographer = dance
                        .select(".listTitle > .listTitleColor2")
                        .text()
                        .replaceAll("\\s*\\([^)]*\\)", "")
                        .split(" -")[0].trim();

                // Song info query
                String info = dance.select(".listInfo > p").text();

                // Split on " - " to separate movement info and music info
                String[] parts = info.split(" - ");
                String movement = parts[0].trim();
                String musicInfo = parts.length > 1 ? parts[1].replaceFirst("Music:\\s*", "").trim() : "";

                //Use regex to extract step and wall count
                Pattern movementPattern = Pattern.compile("(\\d+)\\s+Count\\s+(\\d+|-)\\s+Wall");
                Matcher matcher = movementPattern.matcher(movement);

                if (matcher.find()) {
                    int stepCount = Integer.parseInt(matcher.group(1));
                    int wallCount = 0;

                    String wallGroup = matcher.group(2);
                    if (!wallGroup.equals("-")) {
                        wallCount = Integer.parseInt(wallGroup);
                    } else {
                        System.out.println("Wall count is missing for: " + title);
                    }

                    if (stepCount <= 32) {
                        System.out.println("Dance: " + title);
                        System.out.println("Choreographer(s): " + choreographer);
                        System.out.println("Step Count: " + stepCount);
                        System.out.println("Walls: " + wallCount);
                        System.out.println("Music: " + musicInfo);
                        System.out.println("=======================================");
                    }
                } else {
                    System.out.println("Failed to parse movement info: " + movement);
                }
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
