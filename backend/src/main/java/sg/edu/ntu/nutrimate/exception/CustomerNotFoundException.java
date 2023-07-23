package sg.edu.ntu.nutrimate.exception;

public class CustomerNotFoundException extends RuntimeException {
    
    public CustomerNotFoundException(String userId) {
        super("Customer with userId " + userId + " not found.");
    }
    
    public CustomerNotFoundException(String userId, String email) {
        super("Customer with userId " + userId + " not found.");
    } 
    
}
