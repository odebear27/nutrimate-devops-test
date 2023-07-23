package sg.edu.ntu.nutrimate.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.ntu.nutrimate.entity.Administrator;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.logger.LogHandler;
import sg.edu.ntu.nutrimate.logger.LogHandler.Level;
import sg.edu.ntu.nutrimate.repository.AdministratorRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
     @Autowired
     private CustomerRepository customerRepository;

     @Autowired
     private AdministratorRepository administratorRepository;

     @Autowired
     LoginAttemptService loginAttemptService;

     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

          if (loginAttemptService.isBlocked()) {
               LogHandler.handleLog(Level.INFO, "Blocked");
               
               //If use generic runtime exception, an error will occur as UsernamePasswordAuthenticationFilter
               //is anticipating UsernameNotFoundException instead.
               //UsernamePasswordAuthenticationFilter - An internal error occurred while trying to authenticate the user.
               //org.springframework.security.authentication.InternalAuthenticationServiceException: blocked

               // throw new RuntimeException("blocked"); 
               throw new UsernameNotFoundException("User Blocked");
          } 

          Optional<Customer> customerContainer = customerRepository.findByUserID(username.toLowerCase());
          Optional<Administrator> adminContainer = administratorRepository.findByEmail(username.toLowerCase());

          if (!customerContainer.isPresent()) {
               customerContainer = customerRepository.findByEmail(username.toLowerCase());
          }
          
          if (!customerContainer.isPresent() && !adminContainer.isPresent()) {
               throw new UsernameNotFoundException("Invalid User");
               // return new AuthenticatedUser("", new BCryptPasswordEncoder().encode("nonUserPassword"), "", true);
          }

          if(customerContainer.isPresent()){
               Customer retrievedCustomer = customerContainer.get();
               return new AuthenticatedUser(username, retrievedCustomer.getPassword(), retrievedCustomer.getUserRole(),
                    true);
          }
          else{
               Administrator retrievedAdmin = adminContainer.get();
               return new AuthenticatedUser(username, retrievedAdmin.getPassword(), retrievedAdmin.getUserRole(),
                    true);
          }
     } 

}
