import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

// Server class implementation
public class CodeFixerGameServer extends UnicastRemoteObject implements CodeFixerGame {
    private static final Map<String, List<ErrorCode>> errorCodes = new HashMap<>();
    private final Map<String, Player> players = new HashMap<>(); // Store player data

    // Static block to initialize error codes
    static {
        errorCodes.put("beginner", Arrays.asList(
                new ErrorCode("int x = ;", "int x = 0;"),
                new ErrorCode("String name = \"Alice;", "String name = \"Alice\";"),
                new ErrorCode("if (x = 5)", "if (x == 5)"),
                new ErrorCode("int[] arr = new int[5]; arr[5] = 10;", "int[] arr = new int[5]; arr[4] = 10;"),
                new ErrorCode("System.out.println(\"Hello World\"", "System.out.println(\"Hello World\");")
        ));

        errorCodes.put("intermediate", Arrays.asList(
                new ErrorCode("int[] nums = {1, 2, 3}; System.out.println(nums[3]);", "int[] nums = {1, 2, 3}; System.out.println(nums[2]);")
        ));

        errorCodes.put("advanced", Arrays.asList(
                new ErrorCode("int result = 5 / 0;", "int result = 5 / 1;")
        ));
    }

    // Constructor
    public CodeFixerGameServer() throws RemoteException {
        super();
    }

    @Override
    public String registerPlayer(String username, String level) throws RemoteException {
        if (players.containsKey(username)) {
            return "Username already exists. Choose a different one.";
        }
        if (!errorCodes.containsKey(level.toLowerCase())) {
            return "Invalid level. Please select beginner, intermediate, or advanced.";
        }
        players.put(username, new Player(username, level.toLowerCase(), 0));
        return "Player registered successfully!";
    }

    @Override
    public String fetchChallenge(String username) throws RemoteException {
        Player player = players.get(username);
        if (player == null) return "Player not found. Please register first.";

        List<ErrorCode> challenges = errorCodes.get(player.getLevel());
        if (challenges.isEmpty()) {
            return "No challenges available for your level.";
        }
        ErrorCode challenge = challenges.get(new Random().nextInt(challenges.size()));
        player.setCurrentChallenge(challenge); // Assign the challenge to the player
        return "Fix the following error:\n" + challenge.getErrorMessage();
    }

    @Override
    public String submitFix(String username, String solution) throws RemoteException {
        Player player = players.get(username);
        if (player == null || player.getCurrentChallenge() == null) {
            return "No active challenge found. Fetch a challenge first.";
        }
        if (solution.trim().equals(player.getCurrentChallenge().getCorrectFix())) {
            player.incrementScore();
            player.setCurrentChallenge(null); // Reset current challenge
            return "Correct! Your score is now: " + player.getScore();
        } else {
            return "Incorrect. Try again.";
        }
    }

    @Override
    public String getLeaderboard() throws RemoteException {
        StringBuilder leaderboard = new StringBuilder("Leaderboard:\n");
        players.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                .forEach(player -> leaderboard.append(player.getUsername())
                                              .append(": ")
                                              .append(player.getScore())
                                              .append("\n"));
        return leaderboard.toString();
    }

    // Main method to start the server
    public static void main(String[] args) {
        try {
            // Start the RMI registry
            LocateRegistry.createRegistry(1099);

            // Create and bind the server object
            CodeFixerGameServer server = new CodeFixerGameServer();
            Naming.rebind("rmi://localhost:1099/CodeFixerGame", server);

            System.out.println("CodeFixerGameServer is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
