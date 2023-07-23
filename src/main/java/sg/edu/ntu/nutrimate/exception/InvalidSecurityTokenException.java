package sg.edu.ntu.nutrimate.exception;

public class InvalidSecurityTokenException extends RuntimeException {

    public InvalidSecurityTokenException(String message){
        super(message);
    }
    
}
