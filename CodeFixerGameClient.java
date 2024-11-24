import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class CodeFixerGameClient {
    public static void main(String[] args) {
        try {
            // Connect to the RMI registry on localhost at port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            
            // Look up the remote object with the name CodeFixerGame
            CodeFixerGame game = (CodeFixerGame) registry.lookup("CodeFixerGame");

            Scanner scanner = new Scanner(System.in);

            // Register player
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Select your level (beginner/intermediate/advanced): ");
            String level = scanner.nextLine();

            // Register the player with the selected level
            String registrationResponse = game.registerPlayer(username, level);
            System.out.println(registrationResponse);

            if (registrationResponse.contains("successfully")) {
                // Fetch and display the challenge after registration
                String challengeMessage = game.fetchChallenge(username);
                System.out.println(challengeMessage);  // Display the challenge

                // Automatically ask the user to submit their fix
                System.out.print("Enter your fix: ");
                String fix = scanner.nextLine();

                // Submit the fix and display result
                String submissionResponse = game.submitFix(username, fix);
                System.out.println(submissionResponse);  // Show result (correct or incorrect)

                // Show the next menu
                while (true) {
                    System.out.println("\n1. View Leaderboard\n2. Exit");
                    System.out.print("Choose an option: ");
                    String input = scanner.nextLine();

                    int choice;
                    try {
                        choice = Integer.parseInt(input); // Convert string input to int
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number from 1 to 2.");
                        continue; // If invalid, go back to the menu
                    }

                    if (choice == 1) {
                        // View the leaderboard
                        System.out.println(game.getLeaderboard());
                    } else if (choice == 2) {
                        // Exit the game
                        System.out.println("Exiting the game.");
                        break; // Exit the loop
                    } else {
                        System.out.println("Invalid choice. Please choose between 1 and 2.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
