package com.techelevator.Model;

import java.util.ArrayList;
import java.util.List;

public class UserDanceManager {
    private List<UserDance> userDances = new ArrayList<UserDance>();

    public void addDance(UserDance userDance) {
        userDances.add(userDance);
    }

    public List<UserDance> getUserDances() {
        return userDances;
    }
    
    public void removeDance(UserDance userDance) {
        userDances.remove(userDance);
    }
    public void printDances() {
        for (UserDance userDance : userDances) {
            System.out.println(userDance);
            if (userDances.size() > 1) {
                System.out.println("----------------------------------------------------------");
            }
        }
    }

}
