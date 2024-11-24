import java.io.Serializable;

public class ErrorCode implements Serializable {
    private final String errorMessage;
    private final String correctFix;

    public ErrorCode(String errorMessage, String correctFix) {
        this.errorMessage = errorMessage;
        this.correctFix = correctFix;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getCorrectFix() {
        return correctFix;
    }
}
