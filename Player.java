import java.io.Serializable;

public class Player implements Serializable {
    private final String username;
    private final String level;
    private int score;
    private ErrorCode currentChallenge;

    public Player(String username, String level, int score) {
        this.username = username;
        this.level = level;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public String getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

    public ErrorCode getCurrentChallenge() {
        return currentChallenge;
    }

    public void setCurrentChallenge(ErrorCode currentChallenge) {
        this.currentChallenge = currentChallenge;
    }
}
