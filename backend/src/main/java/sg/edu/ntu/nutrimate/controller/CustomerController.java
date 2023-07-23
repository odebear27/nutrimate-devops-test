package sg.edu.ntu.nutrimate.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.nutrimate.entity.Administrator;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.PasswordDataObject;
import sg.edu.ntu.nutrimate.service.CustomerService;

@RestController
@RequestMapping("/nutrimate")
public class CustomerController {

    @Autowired
    CustomerService customerService;
    
    @PostMapping("/public/create")
    public ResponseEntity<Customer> createCustomerAccount(@Valid @RequestBody Customer customer){
        Customer newCustomer = customerService.createCustomerAccount(customer);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Administrator> createAdminAccount(@Valid @RequestBody Administrator administrator){
        Administrator newAdmin = customerService.createAdministratorAccount(administrator);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    // @GetMapping("/customers/profile")
    // public ResponseEntity<Customer> retrieveCustomerProfile(@RequestParam(required = false, defaultValue = "unknown") 
    // String userID, @RequestParam(required = false, defaultValue = "unknown") String email){
    //     Customer customer = customerService.getCustomerProfile(userID, email);
    //     return new ResponseEntity<>(customer, HttpStatus.OK);
    // }

    @GetMapping("/customers/profile")
    public ResponseEntity<Customer> retrieveCustomerProfile() {
        Customer customer = customerService.getCustomerProfile();
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<Customer> deleteCustomerAccount(@RequestParam String username){
        customerService.deleteCustomerAccount(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @PostMapping("customers/{userID}/changepassword")    
    // public ResponseEntity<Customer> updateCustomerAccountPassword(@PathVariable String userID, @RequestParam(required = true) String password){
    //     customerService.updateCustomerAccountPassword(userID, password);
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }

    @PostMapping("customers/changepassword")    
    public ResponseEntity<Customer> updateCustomerAccountPassword(@Valid @RequestBody PasswordDataObject passwordDTO){
        customerService.updateCustomerAccountPassword(passwordDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // @PutMapping("customers/{userID}/updateprofile")    
    // public ResponseEntity<Customer> updateCustomerProfile (@PathVariable String userID, @Valid @RequestBody Customer customer ){
    //     customerService.updateCustomerProfile(userID, customer);
    //     return new ResponseEntity<>(HttpStatus.OK);
    // }

    @PutMapping("customers/updateprofile")    
    public ResponseEntity<Customer> updateCustomerProfile (@Valid @RequestBody Customer customer ){
        customerService.updateCustomerProfile(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("public/forgetpassword")    
    public ResponseEntity<Customer> handleCustomerRestPassword (HttpServletRequest request, @RequestParam(required = false, defaultValue = "unknown") String username){
        customerService.resetCustomerAccountPassword(username, getAppUrl(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("public/changepassword")    
    public ResponseEntity<Customer> handleCustomerResetChangePassword (@RequestParam(required = false, defaultValue = "unknown") String token, @RequestBody PasswordDataObject passwordDTO){
        passwordDTO.setToken(token);
        customerService.changeCustomerAccountPassword(passwordDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }     

}
