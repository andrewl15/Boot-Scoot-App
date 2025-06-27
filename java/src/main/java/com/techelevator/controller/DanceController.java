package com.techelevator.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.techelevator.dao.DanceDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Dance;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/dance")
public class DanceController {
    private DanceDao danceDao;
    private UserDao userDao;

    public DanceController(DanceDao danceDao, UserDao userDao) {
        this.danceDao = danceDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/{id}")
    public Dance getCampaignById(@PathVariable int id) {
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
    public Dance addNewCampaign(@Valid @RequestBody Dance incoming, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be authenticated");
        }

        try {
            int userId = userDao.getUserByUsername(principal.getName()).getId();
            return danceDao.addDance(userId, incoming);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }
}
