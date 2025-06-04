package com.techelevator;

import com.techelevator.Model.UserDance;
import com.techelevator.Model.UserDanceManager;
import com.techelevator.Services.Scraper;
import com.techelevator.Services.UserDanceMenu;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DanceSearchApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scraper scraper = new Scraper();
        UserDanceManager userDanceManager = new UserDanceManager();
        UserDanceMenu userMenu = new UserDanceMenu();

        System.out.println("🎵 Welcome to Line Dance Finder!");
        boolean running = true;
        while (running) {
            System.out.println("\nPlease select an option:");
            System.out.println("1) Dance Search");
            System.out.println("2) User Dance Menu");
            System.out.println("3) Exit");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.print("\nEnter a song or dance name to search: ");
                    String query = scanner.nextLine();
                    try {
                        List<Scraper.DancePreview> results = scraper.searchDances(query);
                        if (results.isEmpty()) {
                            System.out.println("No results found.");
                            continue;
                        }

                        // Display choices
                        for (int i = 0; i < results.size(); i++) {
                            Scraper.DancePreview d = results.get(i);
                            System.out.printf("%d. %s by %s%n", i + 1, d.getTitle(), d.getChoreographer());
                            System.out.printf("   Steps: %s | Walls: %s | Level: %s%n", d.getStepCount(), d.getWallCount(), d.getLevel());
                            System.out.printf("   Music: %s%n%n", d.getMusic());
                        }

                        System.out.print("\nSelect a dance number to view details: ");
                        int choice = Integer.parseInt(scanner.nextLine()) - 1;

                        if (choice >= 0 && choice < results.size()) {
                            UserDance dance = scraper.getDanceDetails(results.get(choice));
                            System.out.println("\nDance Details:");
                            System.out.println(dance);

                            System.out.print("\nWould you like to add this dance to your list? (yes/no): ");
                            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                                userDanceManager.addDance(dance);
                                System.out.println("Dance saved!");
                            }
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } catch (IOException e) {
                        System.out.println("Error fetching data. Try again.");
                        e.printStackTrace();

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                    break;
                case "2":
                    String userInput = userMenu.PrintMainUserDanceMenu();
                    switch (userInput) {
                        case "1":
                            userDanceManager.printDances();
                            break;
                        case "2":
                            userDanceManager.printDanceNames();
                            System.out.println("Please choose the song to be checked off: ");
                            userInput = scanner.nextLine();
                            userDanceManager.MarkDanceAsLearned(userDanceManager.getUserDance(Integer.parseInt(userInput) - 1));
                            break;
                        case "3":
                            userDanceManager.printDanceNames();
                            System.out.println("Please choose the song to be removed");
                            userInput = scanner.nextLine();
                            userDanceManager.removeDance(userDanceManager.getUserDance(Integer.parseInt(userInput) - 1));
                            break;
                        case "4":
                            System.out.println("Returning to Main Menu");
                            break;
                    }
                    break;
                case "3":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }

        }

        System.out.println("\nGoodbye! 👋");
        scanner.close();
    }
}
