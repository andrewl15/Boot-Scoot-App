package com.techelevator.Services;

import java.util.Scanner;

public class UserDanceMenu {
    public String PrintMainUserDanceMenu(Scanner scanner) {
        System.out.println("User Dance Menu");
        System.out.println("1) View Your Dance List");
        System.out.println("2) Mark Dance as Learned");
        System.out.println("3) Remove Dance from list");
        System.out.println("4) Return to Main Menu");
        return scanner.nextLine();
    }
}
