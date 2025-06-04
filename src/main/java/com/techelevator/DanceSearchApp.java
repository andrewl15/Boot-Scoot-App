package com.techelevator;

import com.techelevator.Model.UserDance;
import com.techelevator.Model.UserDanceManager;
import com.techelevator.Services.Scraper;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DanceSearchApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scraper scraper = new Scraper();
        UserDanceManager userDanceManager = new UserDanceManager();

        System.out.println("🎵 Welcome to Line Dance Finder!");
        boolean running = true;
        while (running) {
            System.out.println("\nPlease select an option:");
            System.out.println("1) Find Dances");
            System.out.println("2) View Your List of Dances");
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
                            System.out.println("\n📋 Dance Details:");
                            System.out.println(dance);

                            System.out.print("\nWould you like to save this dance? (yes/no): ");
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
                    System.out.println("\n💾 Saved Dances:");
                    userDanceManager.printDances();
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
