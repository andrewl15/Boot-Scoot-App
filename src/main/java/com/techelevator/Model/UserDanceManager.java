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

    public UserDance getUserDance(int index) {
        return userDances.get(index);
    }

    public void MarkDanceAsLearned(UserDance userDance) {
        userDance.setLearned(true);
        System.out.println(userDance.getTitle() + " is learned!");
    }
    
    public void removeDance(UserDance userDance) {
        userDances.remove(userDance);
        System.out.println(userDance.getTitle() + " is removed!");
    }

    public void printDances() {
        int counter = 1;
        for (UserDance userDance : userDances) {
            System.out.println("Dance " + counter + ")");
            System.out.println();
            System.out.println(userDance);
            counter++;
            if (counter > 1) {
                System.out.println("----------------------------------------------------------");
            }
        }
    }

    public void printDanceNames() {
        int counter = 1;
        for (UserDance userDance : userDances) {
            System.out.println(counter + ") " + userDance.getTitle());
            counter++;
        }
    }

}
