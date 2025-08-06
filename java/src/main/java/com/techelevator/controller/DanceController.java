package com.techelevator.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.techelevator.dao.DanceDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Dance;
import com.techelevator.model.DanceLinkRequest;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/dance")
public class DanceController {
    private DanceDao danceDao;
    private UserDao userDao;

    public DanceController(DanceDao danceDao, UserDao userDao) {
        this.danceDao = danceDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/{id}")
    public Dance getDanceById(@PathVariable int id) {
        Dance output = null;

        try {
            output = danceDao.getDanceById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        if (output == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dance not found");
        }
        return output;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public Dance addNewDance(@Valid @RequestBody DanceLinkRequest request, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }

        try {
            int userId = userDao.getUserByUsername(principal.getName()).getId();

            // Scrape the CopperKnob page and build a Dance object
            Dance scrapedDance = scrapeDanceFromCopperknob(request.url, userId);

            // Save the dance to the DB
            return danceDao.addDance(userId, scrapedDance);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping("/userDanceList/{userId}")
    public List<Dance> getDancesByUserId(@PathVariable int userId) {
        try {
            return danceDao.getDancesByUserId(userId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public Dance updateDance(@PathVariable int id, @Valid @RequestBody Dance incoming) {
        Dance existing = danceDao.getDanceById(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dance not found");
        }
        try {
            return danceDao.updateDance(incoming);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    public Dance scrapeDanceFromCopperknob(String url, int userId) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();
        //dance id
        int copperknobId = Integer.parseInt(extractDanceIdFromUrl(url));
        // Title
        String danceName = doc.selectFirst("div.sectionbar h2") != null
                ? doc.selectFirst("div.sectionbar h2").text().trim()
                : "";

        int count = 0;
        int walls = 0;
        String level = "";
        String choreographer = "";
        String songName = "";
        String artistName = "";

        // Count
        Element countElem = doc.selectFirst(".sheetinfocount span");
        if (countElem != null) {
            try {
                count = Integer.parseInt(countElem.text().trim());
            } catch (NumberFormatException ignored) {
            }
        }

        // Walls
        Element wallElem = doc.selectFirst(".sheetinfowall span");
        if (wallElem != null) {
            try {
                walls = Integer.parseInt(wallElem.text().trim());
            } catch (NumberFormatException ignored) {
            }
        }

        // Level
        Element levelElem = doc.selectFirst(".sheetinfolevel span");
        if (levelElem != null) {
            level = levelElem.text().trim();
        }

        // Choreographer
        Element choreoElem = doc.selectFirst(".sheetinfochoregrapher span");
        if (choreoElem != null) {
            choreographer = choreoElem.text().trim();
        }

        // Song Name (First <a> in music section)
        Element songElem = doc.selectFirst(".sheetinfomusic span a");
        if (songElem != null) {
            songName = songElem.text().trim();
        }

        // Artist Name (extract from text after the <a>)
        Element artistElem = doc.selectFirst(".sheetinfomusic span");
        if (artistElem != null) {
            String[] parts = artistElem.text().split(" - ");
            if (parts.length >= 2) {
                artistName = parts[1].trim();
            }
        }

        String demoUrl = "";
        String tutorialUrl = "";

        Elements videoContainers = doc.select("div.videothumb");

        for (Element container : videoContainers) {
            Element thumb = container.selectFirst(".videothumbimage");
            if (thumb == null)
                continue;

            String style = thumb.attr("style");
            String videoId = extractVideoIdFromStyle(style);
            if (videoId.isEmpty())
                continue;

            String fullUrl = "https://www.youtube.com/watch?v=" + videoId;

            if (container.selectFirst(".videothumbdemo") != null && demoUrl.isEmpty()) {
                demoUrl = fullUrl;
            } else if (container.selectFirst(".videothumbteach") != null && tutorialUrl.isEmpty()) {
                tutorialUrl = fullUrl;
            }

            // Exit early if both URLs found
            if (!demoUrl.isEmpty() && !tutorialUrl.isEmpty()) {
                break;
            }
        }

        return new Dance(
                copperknobId, // danceId - assumed generated in DB
                userId,
                false, // isLearned defaults to false
                danceName,
                songName,
                artistName,
                count,
                walls,
                level,
                url,
                demoUrl,
                tutorialUrl);
    }

    private String extractVideoIdFromStyle(String style) {
        Pattern pattern = Pattern.compile("vi/([\\w-]{11})/");
        Matcher matcher = pattern.matcher(style);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    private String extractDanceIdFromUrl(String url) {
        Pattern pattern = Pattern.compile("/stepsheets/(\\d+)/");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

}
