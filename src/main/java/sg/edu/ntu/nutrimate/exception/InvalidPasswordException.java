package sg.edu.ntu.nutrimate.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
    
}
