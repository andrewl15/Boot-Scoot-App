package com.techelevator.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Dance;

@Component
public class JdbcDanceDao implements DanceDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcDanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Get a dance by its global dance_id
     */
    public Dance getDanceById(int danceId) {
        String sql = "SELECT * FROM dance WHERE dance_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, danceId);
            if (results.next()) {
                return mapRowToDance(results);
            }
            return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    /**
     * Add a new global dance (if it doesn’t exist)
     */
    public Dance addGlobalDance(Dance dance) {
        String sql = "INSERT INTO dance (dance_name, song_name, artist_name, count, walls, level, copperknob_link, demo_url, tutorial_url) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING dance_id;";
        try {
            int danceId = jdbcTemplate.queryForObject(sql, int.class,
                    dance.getDanceName(),
                    dance.getSongName(),
                    dance.getArtistName(),
                    dance.getCount(),
                    dance.getWalls(),
                    dance.getLevel(),
                    dance.getCopperknobLink(),
                    dance.getDemoUrl(),
                    dance.getTutorialUrl());
            return getDanceById(danceId);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Dance already exists or invalid data: " + e.getMessage());
        }
    }

    @Override
    public Dance addDance(int userId, Dance dance) {
        Dance newDance = null;

        String insertDanceSql = "INSERT INTO dance (dance_id, dance_name, song_name, artist_name, count, walls, level, copperknob_link, demo_url, tutorial_url) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (dance_id) DO NOTHING;";

        String insertUserDanceSql = "INSERT INTO user_dance (user_id, dance_id, is_learned) VALUES (?, ?, ?) " +
                "ON CONFLICT (user_id, dance_id) DO NOTHING;";

        try {
            // Insert dance if it doesn't already exist
            jdbcTemplate.update(insertDanceSql,
                    dance.getDanceId(),
                    dance.getDanceName(),
                    dance.getSongName(),
                    dance.getArtistName(),
                    dance.getCount(),
                    dance.getWalls(),
                    dance.getLevel(),
                    dance.getCopperknobLink(),
                    dance.getDemoUrl(),
                    dance.getTutorialUrl());

            // Link dance to user
            jdbcTemplate.update(insertUserDanceSql, userId, dance.getDanceId(), false);

            // Retrieve and return the Dance
            newDance = getDanceById(dance.getDanceId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new DaoException("Unexpected error adding dance: " + e.getMessage(), e);
        }

        return newDance;
    }

    /**
     * Associate a dance with a user (with default learned=false)
     */
    public void addUserDance(int userId, int danceId) {
        String sql = "INSERT INTO user_dance (user_id, dance_id) VALUES (?, ?) ON CONFLICT DO NOTHING;";
        try {
            jdbcTemplate.update(sql, userId, danceId);
        } catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e) {
            throw new DaoException("Unable to add user dance: " + e.getMessage(), e);
        }
    }

    /**
     * Get all dances for a specific user, including learned status
     */
    public List<Dance> getDancesByUserId(int userId) {
        List<Dance> dances = new ArrayList<>();
        String sql = "SELECT d.*, ud.is_learned " +
                "FROM dance d " +
                "JOIN user_dance ud ON d.dance_id = ud.dance_id " +
                "WHERE ud.user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                dances.add(mapRowToDance(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return dances;
    }

    /**
     * Update learned status for a user's dance
     */
    public void updateLearnedStatus(int userId, int danceId, boolean isLearned) {
        String sql = "UPDATE user_dance SET is_learned = ? WHERE user_id = ? AND dance_id = ?;";
        try {
            jdbcTemplate.update(sql, isLearned, userId, danceId);
        } catch (CannotGetJdbcConnectionException | DataIntegrityViolationException e) {
            throw new DaoException("Unable to update learned status: " + e.getMessage(), e);
        }
    }

    /**
     * Helper to map a SQL row to a Dance object
     */
    private Dance mapRowToDance(SqlRowSet results) {
        Dance dance = new Dance();
        dance.setDanceId(results.getInt("dance_id"));
        dance.setDanceName(results.getString("dance_name"));
        dance.setSongName(results.getString("song_name"));
        dance.setArtistName(results.getString("artist_name"));
        dance.setCount(results.getInt("count"));
        dance.setWalls(results.getInt("walls"));
        dance.setLevel(results.getString("level"));
        dance.setCopperknobLink(results.getString("copperknob_link"));
        dance.setDemoUrl(results.getString("demo_url"));
        dance.setTutorialUrl(results.getString("tutorial_url"));
        // If is_learned column exists (from join with user_dance)
        try {
            dance.setLearned(results.getBoolean("is_learned"));
        } catch (Exception e) {
            dance.setLearned(false);
        }
        return dance;
    }
}
