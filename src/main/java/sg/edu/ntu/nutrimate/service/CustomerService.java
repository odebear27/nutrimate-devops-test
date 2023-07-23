package sg.edu.ntu.nutrimate.service;

import sg.edu.ntu.nutrimate.entity.Administrator;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.PasswordDataObject;

public interface CustomerService {
    public Customer createCustomerAccount(Customer customer);
    public Administrator createAdministratorAccount(Administrator administrator);
    public Customer getCustomerProfile(String userID, String userEmail);
    public Customer getCustomerProfile();
    public Customer updateCustomerAccountPassword(PasswordDataObject passwordDTO);
    public void deleteCustomerAccount(String userID);
    public Customer updateCustomerProfile(String userID, Customer customer);
    public Customer updateCustomerProfile(Customer customer);
    public void resetCustomerAccountPassword(String userID, String appURL);
    public void changeCustomerAccountPassword(PasswordDataObject passwordDTO);
}
