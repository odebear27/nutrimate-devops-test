package sg.edu.ntu.nutrimate.exception;

public class CustomerEntityNotUniqueException extends RuntimeException{
    public CustomerEntityNotUniqueException(String entityStatusMessage){
        super(entityStatusMessage);
    }
}
