package sg.edu.ntu.nutrimate.security;

import java.util.Optional;

import sg.edu.ntu.nutrimate.entity.Customer;

public interface UserSecurityService {

    String createPasswordResetTokenForUser(Customer customer);
    String validatePasswordResetToken(String token);
    Optional<Customer> getUserByPasswordResetToken(String token);
    String changePassword(String password);
    boolean checkIfValidOldPassword(String retrievedOldPassword, String providedOldPassword);
    boolean checkIfValidPassword(String retrievedPassword, String providedPassword);        
}
