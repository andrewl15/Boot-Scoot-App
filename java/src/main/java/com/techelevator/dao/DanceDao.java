package com.techelevator.dao;

import java.util.List;

import com.techelevator.model.Dance;

public interface DanceDao {
    Dance getDanceById(int id);
    Dance addDance(int userId, Dance dance);
    List<Dance> getDancesByUserId(int userId);
    
}
