package com.techelevator.dao;

import java.util.List;
import com.techelevator.model.Dance;

public interface DanceDao {

    // Returns the dance info by its ID (from dance table)
    Dance getDanceById(int danceId);

    // Adds a dance for a user (creates an entry in dance + user_dance)
    Dance addDance(int userId, Dance dance);

    // Get all dances for a specific user, including learned status
    List<Dance> getDancesByUserId(int userId);

    // Update the learned status for a specific dance for a specific user
    void updateLearnedStatus(int userId, int danceId, boolean isLearned);
}
