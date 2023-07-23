package sg.edu.ntu.nutrimate.exception;

public class CustomerNoPermissionException extends RuntimeException {

    public CustomerNoPermissionException(String message) {
        super(message);
    }

}
