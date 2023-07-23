package sg.edu.ntu.nutrimate.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import sg.edu.ntu.nutrimate.entity.Administrator;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.CustomerEntityStatus;
import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.entity.PasswordDataObject;
import sg.edu.ntu.nutrimate.entity.PasswordResetToken;
import sg.edu.ntu.nutrimate.exception.AdministratorNotUniqueException;
import sg.edu.ntu.nutrimate.exception.CustomerEntityNotUniqueException;
import sg.edu.ntu.nutrimate.exception.CustomerNotFoundException;
import sg.edu.ntu.nutrimate.exception.InvalidPasswordException;
import sg.edu.ntu.nutrimate.exception.InvalidSecurityTokenException;
import sg.edu.ntu.nutrimate.exception.ParameterBadRequestException;
import sg.edu.ntu.nutrimate.logger.LogHandler;
import sg.edu.ntu.nutrimate.logger.LogHandler.Level;
import sg.edu.ntu.nutrimate.repository.AdministratorRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.repository.PasswordResetTokenRepository;
import sg.edu.ntu.nutrimate.repository.RecipeRepository;
import sg.edu.ntu.nutrimate.security.AuthenticatedUser;
import sg.edu.ntu.nutrimate.security.UserSecurityService;
import sg.edu.ntu.nutrimate.utility.EmailService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Validated
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    PasswordResetTokenRepository pwResetTokenRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
	private ObjectMapper objectMapper; //convert java objects to json and vice verse

    @Autowired
    public EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserSecurityService userSecurityService;

    @Override
    public Customer createCustomerAccount(Customer customer) {
        Optional<Customer> customerUserIdContainer = customerRepository.findByUserID(customer.getUserID().toLowerCase());
        Optional<Customer> customerEmailContainer = customerRepository.findByEmail(customer.getEmail().toLowerCase());

        if(customerUserIdContainer.isPresent() || customerEmailContainer.isPresent()){
            CustomerEntityStatus status = new CustomerEntityStatus();
            if(customerUserIdContainer.isPresent()){
                status.setUserIDExist(true);
            }
            
            if(customerEmailContainer.isPresent()){
                status.setEmailExist(true);
            }
                       
            try {
                String customerEntityStatus = objectMapper.writeValueAsString(status);
                throw new CustomerEntityNotUniqueException(customerEntityStatus);
            } catch (JsonProcessingException e) {
                LogHandler.handleLog(Level.ERROR, e.getMessage());
            }
        }
        
        Customer newCustomer = new Customer();
        newCustomer.setEmail(customer.getEmail().toLowerCase());
        newCustomer.setUserID(customer.getUserID().toLowerCase());

        newCustomer.setFirstName(customer.getFirstName().toUpperCase());
        newCustomer.setLastName(customer.getLastName().toUpperCase());  

        newCustomer.setPassword(passwordEncoder.encode(customer.getPassword()));
        newCustomer.setUserRole("user");
        newCustomer.setAddress(customer.getAddress());
        newCustomer.setContactNo(customer.getContactNo());

        customerRepository.save(newCustomer);

        return newCustomer;
    }

    @Override
    public Administrator createAdministratorAccount(Administrator administrator) {
       Optional<Administrator> adminContainer = administratorRepository.findByEmail(administrator.getEmail().toLowerCase());

        if(adminContainer.isPresent()){
            throw new AdministratorNotUniqueException("Administrator email exists");
        }         
               
        Administrator newAdmin = new Administrator();
        newAdmin.setEmail(administrator.getEmail().toLowerCase());
        newAdmin.setUserID(administrator.getUserID().toLowerCase());

        newAdmin.setFirstName(administrator.getFirstName().toUpperCase());
        newAdmin.setLastName(administrator.getLastName().toUpperCase());  

        newAdmin.setPassword(passwordEncoder.encode(administrator.getPassword()));
        newAdmin.setUserRole("admin");
        newAdmin.setContactNo(administrator.getContactNo());

        administratorRepository.save(newAdmin);

        return newAdmin;
    }    

    @Override
    public Customer getCustomerProfile() {
        Customer authenticatedCustomer = getAuthenticatedCustomer();
        authenticatedCustomer.setPassword("");

        return authenticatedCustomer;
    }  

    @Override
    public Customer getCustomerProfile(String userID, String userEmail) {

        boolean isCustomerFound = false;        
        
        if(userID.equals("unknown") && userEmail.equals("unknown") ){
            throw new RuntimeException("User ID or email not provided");
        }
        else{
            Optional<Customer> customerContainer = customerRepository.findByUserID(userID.toLowerCase());

            isCustomerFound = customerContainer.isPresent();

            if(!isCustomerFound){
                customerContainer = customerRepository.findByEmail(userEmail.toLowerCase());
                isCustomerFound = customerContainer.isPresent();
            }
            
            if(!isCustomerFound){
                throw new RuntimeException("Customer not found");
            }

            Customer retrievedCustomer = customerContainer.get();
            retrievedCustomer.setPassword("");

            return retrievedCustomer;

        }          
        
    }

    @Override
    @Transactional
    public void deleteCustomerAccount(String username) {

        Optional<Customer> customerContainer = customerRepository.findByUserID(username.toLowerCase());

        if(!customerContainer.isPresent()){
            customerContainer = customerRepository.findByEmail(username.toLowerCase());
        }

        if(!customerContainer.isPresent()){
             throw new RuntimeException("Invalid UserID or User Email");
        } 
       
        Customer retrievedCustomer = customerContainer.get();

        checkifCustomerExistOtherRepository(retrievedCustomer);
        
        customerRepository.deleteByUserID(retrievedCustomer.getUserID());
    }

    @Override
    public Customer updateCustomerAccountPassword(PasswordDataObject passwordDTO) {

        Customer authenticatedCustomer = getAuthenticatedCustomer();

        String oldPassword = passwordDTO.getOldPassword();
        String newPassword = passwordDTO.getNewPassword();

        if(!userSecurityService.checkIfValidOldPassword(authenticatedCustomer.getPassword(), oldPassword)){
            throw new InvalidPasswordException("Invalid existing passowrd");
        }        
        
        if(newPassword.isEmpty()){
            throw new ParameterBadRequestException("Valid password is required");
        }

        if(newPassword.length() < 8){
            throw new ParameterBadRequestException("Password must be at least 8");
        }

        authenticatedCustomer.setPassword(userSecurityService.changePassword(newPassword));
        
        String mailTo = authenticatedCustomer.getEmail();
        String mailSubject = "[Password changed] on " + getCurrentDatetime();
        String content = "Your password was initiated to change on the above date and time. If you susepct anyrhing suspicious, please contact our helpdeck immediately";

        emailService.sendSimpleMessage(mailTo, mailSubject, content);

        return customerRepository.save(authenticatedCustomer);
    }

    @Override
    public Customer updateCustomerProfile(Customer customer) {
        Customer authenticatedCustomer = getAuthenticatedCustomer();

        authenticatedCustomer.setFirstName(customer.getFirstName().toUpperCase());
        authenticatedCustomer.setLastName(customer.getLastName().toUpperCase());  

        // existingCustomer.setPassword(userSecurityService.changePassword(customer.getPassword()));
        authenticatedCustomer.setAddress(customer.getAddress());
        authenticatedCustomer.setContactNo(customer.getContactNo());

        return customerRepository.save(authenticatedCustomer);
    }

    @Override
    public Customer updateCustomerProfile(String userID, Customer customer) {
        Optional<Customer> customerUserIdContainer = customerRepository.findByUserID(userID.toLowerCase());
        
        if(!customerUserIdContainer.isPresent()){
            throw new CustomerNotFoundException(userID);
        }

        Customer existingCustomer = customerUserIdContainer.get();

        existingCustomer.setFirstName(customer.getFirstName().toUpperCase());
        existingCustomer.setLastName(customer.getLastName().toUpperCase());  

        // existingCustomer.setPassword(userSecurityService.changePassword(customer.getPassword()));
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setContactNo(customer.getContactNo());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void resetCustomerAccountPassword(String username, String appURL) {

        if(username.equals("unknown")){
            throw new RuntimeException("User ID or email not provided");
        }

        Optional<Customer> customerContainer = customerRepository.findByUserID(username.toLowerCase());

        if(!customerContainer.isPresent()){
            customerContainer = customerRepository.findByEmail(username.toLowerCase());
        }

        if(!customerContainer.isPresent()){
             throw new RuntimeException("Invalid UserID or User Email");
        } 

        Customer retrievedCustomer = customerContainer.get();

        String resetURL = appURL + "/nutrimate/public/changepassword?token=" + userSecurityService.createPasswordResetTokenForUser(retrievedCustomer);
        
        String mailTo = retrievedCustomer.getEmail();
        String mailSubject = "[Password Reset] initiated at " + getCurrentDatetime();;
        String content = "Please click on link below to reset your password \n" + resetURL;

        emailService.sendSimpleMessage(mailTo, mailSubject, content);

    }

    @Override
    public void changeCustomerAccountPassword(PasswordDataObject passwordDTO) {

        String result = userSecurityService.validatePasswordResetToken(passwordDTO.getToken());

        if(result != null){
            throw new InvalidSecurityTokenException(result);
        }

        Optional<Customer> customerContainer = userSecurityService.getUserByPasswordResetToken(passwordDTO.getToken());

        if(!customerContainer.isPresent()){
            throw new InvalidSecurityTokenException("No valid token for user found");
        }

        Customer customer = customerContainer.get();
        customer.setPassword(userSecurityService.changePassword(passwordDTO.getNewPassword()));
        
        customerRepository.save(customer);
    }

    //----------------- Private Methods ------------------------------

    private String getCurrentDatetime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime currentDateTime = LocalDateTime.now();

        return dtf.format(currentDateTime);
    }

    private void checkifCustomerExistOtherRepository(Customer customer){
        Optional<PasswordResetToken> resetTokenContainer = pwResetTokenRepository.findByCustomer(customer);
        List<CustomerRecipe> recipesContainer = recipeRepository.findAllByCustomer(customer);        

        if(resetTokenContainer.isPresent()){
            pwResetTokenRepository.deleteByCustomer(customer);
        }
        
        if(!recipesContainer.isEmpty()){
            recipeRepository.deleteAllByCustomer(customer);
        }
        
    }

    private Customer getAuthenticatedCustomer() {
        String username = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        Optional<Customer> customerContainer = customerRepository.findByUserID(username.toLowerCase());

        if(!customerContainer.isPresent()){
            customerContainer = customerRepository.findByEmail(username.toLowerCase());
        }

        if(!customerContainer.isPresent()){
            throw new BadCredentialsException("Invalid Username");
        }

        return customerContainer.get();
    }

}

