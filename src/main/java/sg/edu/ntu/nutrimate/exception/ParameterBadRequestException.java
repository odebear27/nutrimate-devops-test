package sg.edu.ntu.nutrimate.exception;

public class ParameterBadRequestException extends RuntimeException{
    public ParameterBadRequestException(String message) {
        super(message);       
    }
}
