package sg.edu.ntu.nutrimate.exception;

public class AdministratorNotUniqueException extends RuntimeException {
    public AdministratorNotUniqueException(String message){
        super(message);
    }
}
