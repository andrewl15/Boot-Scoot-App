package com.techelevator.dao;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Dance;

@Component
public class JdbcDanceDao implements DanceDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcDanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Dance getDanceById(int id) {
        Dance dance = null;
        String sql = "select * from dance where dance_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                dance = mapRowToDance(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return dance;
    }

    public Dance addDance(int userId, Dance dance) {
        Dance newDance = null;
        String sql = "INSERT INTO dance (\r\n" + //
                "    user_id,\r\n" + //
                "    is_learned,\r\n" + //
                "    dance_name,\r\n" + //
                "    song_name,\r\n" + //
                "    artist_name,\r\n" + //
                "    count,\r\n" + //
                "    walls,\r\n" + //
                "    level,\r\n" + //
                "    copperknob_link,\r\n" + //
                "    demo_url,\r\n" + //
                "    tutorial_url    \r\n" + //
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?) returning dance_id;";
        try {
            int danceId = jdbcTemplate.queryForObject(sql, int.class,
                dance.getUserId(), dance.isLearned(), dance.getDanceName(),
                dance.getSongName(), dance.getArtistName(), dance.getCount(),
                dance.getWalls(), dance.getLevel(), dance.getCopperknobLink(),
                dance.getDemoUrl(), dance.getTutorialUrl());      

            newDance = getDanceById(danceId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(e.getMessage());
        }
        return newDance;
    }

    public List<Dance> getDancesByUserId(int userId) {
        return null;
    }

    private Dance mapRowToDance(SqlRowSet results) {
        Dance dance = new Dance();
        dance.setDanceId(results.getInt("dance_id"));
        dance.setUserId(results.getInt("user_id"));
        dance.setLearned(results.getBoolean("is_learned"));
        dance.setDanceName(results.getString("dance_name"));
        dance.setSongName(results.getString("song_name"));
        dance.setArtistName(results.getString("artist_name"));
        dance.setCount(results.getInt("count"));
        dance.setWalls(results.getInt("walls"));
        dance.setLevel(results.getString("level"));
        dance.setCopperknobLink(results.getString("copperknob_link"));
        dance.setDemoUrl(results.getString("demo_url"));
        dance.setTutorialUrl(results.getString("tutorial_url"));
        return dance;
    }
}
