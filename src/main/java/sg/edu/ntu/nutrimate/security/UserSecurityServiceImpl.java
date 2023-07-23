package sg.edu.ntu.nutrimate.security;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.ntu.nutrimate.entity.Administrator;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.PasswordResetToken;
import sg.edu.ntu.nutrimate.repository.AdministratorRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.repository.PasswordResetTokenRepository;

@Service
@Transactional
public class UserSecurityServiceImpl implements UserSecurityService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    public String getAuthenticatedUserName() {
        String username = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();

        String userNameFormat = "%s %s";
        String userDisplayName = "anonymous";

        Optional<Customer> customerContainer = customerRepository.findByUserID(username.toLowerCase());
        Optional<Administrator> adminContainer = administratorRepository.findByEmail(username.toLowerCase());

        if (!customerContainer.isPresent()) {
            customerContainer = customerRepository.findByEmail(username.toLowerCase());
        }

        if (!customerContainer.isPresent() && !adminContainer.isPresent()) {
            throw new UsernameNotFoundException("Invalid User");
        }

        if(customerContainer.isPresent()){
            userDisplayName = String.format(userNameFormat, customerContainer.get().getFirstName(), customerContainer.get().getLastName());
        }
        else{
            userDisplayName = String.format(userNameFormat, adminContainer.get().getFirstName(), adminContainer.get().getLastName());
        }

        return userDisplayName;
    }

    @Override
    public String createPasswordResetTokenForUser(Customer customer) {
        String tokenToUser = UUID.randomUUID().toString();
        PasswordResetToken pwResetToken = new PasswordResetToken(tokenToUser, customer);
        passwordResetTokenRepository.save(pwResetToken);

        return tokenToUser;
    }

    @Override
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "Invalid Token"
                : isTokenExpired(passToken) ? "Expired Token"
                        : null;
    }

    @Override
    public Optional<Customer> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getCustomer());
    }

    @Override
    public boolean checkIfValidOldPassword(String retrievedOldPassword, String providedOldPassword) {
        return passwordEncoder.matches(providedOldPassword, retrievedOldPassword);
    }

    @Override
    public boolean checkIfValidPassword(String retrievedPassword, String providedPassword) {
        return passwordEncoder.matches(providedPassword, retrievedPassword);
    }

    @Override
    public String changePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // ----------------- Private Methods ---------------------------

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

}
