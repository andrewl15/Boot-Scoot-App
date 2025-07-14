package com.techelevator.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.model.DanceSearch;

@RestController
@CrossOrigin
@RequestMapping("/danceSearch")
public class DanceSearchController {

    @GetMapping("/search")
    public ResponseEntity<?> searchDances(@RequestParam String query) {
        if (query == null || query.isBlank()) {
            return ResponseEntity.badRequest().body("Missing or empty search query.");
        }

        List<DanceSearch> results = new ArrayList<>();

        try {
            String baseUrl = "https://www.copperknob.co.uk/search";
            String fullUrl = baseUrl + "?Search=" + URLEncoder.encode(query, StandardCharsets.UTF_8)
                    + "&Beat=-1&Level=Any&Wall=-1&Lang=Any&SearchType=Any";
            System.out.println(fullUrl);
            Document doc = Jsoup.connect(fullUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();

            Elements rows = doc.select("div.listitem");

            for (Element row : rows) {
                Element titleElement = row.selectFirst("span.listTitleColor1");
                Element choreoElement = row.selectFirst("span.listTitleColor2");
                Element linkElement = row.selectFirst("a[href]");
                Element infoElement = row.selectFirst("p.listIcons");

                if (titleElement == null || linkElement == null)
                    continue;

                String title = titleElement.text().trim();
                String artist = choreoElement != null ? choreoElement.text().trim() : "";
                String url = linkElement.attr("href");

                if (!url.startsWith("http")) {
                    url = "https://www.copperknob.co.uk" + url;
                }

                int count = 0;
                int wall = 0;
                String level = "";

                if (infoElement != null) {
                    String infoText = infoElement.text(); // Gets cleaned plain text like:
                    // "96 Count 2 Wall Phrased Intermediate Music: Bang Bang - Holy Molly"

                    String[] parts = infoText.split("\\s+");

                    // Look for patterns like "96 Count"
                    try {
                        for (int i = 0; i < parts.length; i++) {
                            if (parts[i].equalsIgnoreCase("Count")) {
                                count = Integer.parseInt(parts[i - 1]);
                            } else if (parts[i].equalsIgnoreCase("Wall")) {
                                wall = Integer.parseInt(parts[i - 1]);
                            } else if (parts[i].equalsIgnoreCase("Music:")) {
                                // Everything before this is part of the level
                                StringBuilder levelBuilder = new StringBuilder();
                                for (int j = i - 1; j >= 0; j--) {
                                    if (parts[j].equalsIgnoreCase("Wall") || parts[j].equalsIgnoreCase("Count"))
                                        break;
                                    levelBuilder.insert(0, parts[j] + " ");
                                }
                                level = levelBuilder.toString().trim();
                            }
                        }
                    } catch (Exception e) {
                        // Log or skip if something breaks
                    }
                }

                results.add(new DanceSearch(title, artist, count, wall, level, url));
            }

            return ResponseEntity.ok(results);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to fetch dance data: " + e.getMessage());
        }
    }
}
