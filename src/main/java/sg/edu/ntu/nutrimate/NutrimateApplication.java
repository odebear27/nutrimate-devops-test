package sg.edu.ntu.nutrimate;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import sg.edu.ntu.nutrimate.entity.Address;
import sg.edu.ntu.nutrimate.entity.Administrator;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.repository.AdministratorRepository;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;

@SpringBootApplication
public class NutrimateApplication {

	@Autowired
	AdministratorRepository administratorRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(NutrimateApplication.class, args);
	}

	@PostConstruct
	public void initiateAdminAccount(){
		Administrator superAdmin = new Administrator();
		superAdmin.setFirstName("Sushmita");
		superAdmin.setLastName("Sen");
		superAdmin.setEmail("superadmin@nutrimate.com.sg");
		superAdmin.setUserID("superadmin");
		superAdmin.setContactNo("98763763");
		superAdmin.setPassword(passwordEncoder.encode("password"));
		superAdmin.setUserRole("admin");

		administratorRepository.save(superAdmin);

		Customer cust1 = new Customer();
		cust1.setFirstName("Jeremy");
		cust1.setLastName("Hillberry");
		cust1.setContactNo("92837463");
		cust1.setEmail("jh@gmail.com");
		cust1.setUserID("jh");
		cust1.setPassword(passwordEncoder.encode("customer1"));
		cust1.setUserRole("user");
		cust1.setAddress(new Address("123", "street 1", "01-01", "", "123456"));

		customerRepository.save(cust1);

		Customer cust2 = new Customer();
		cust2.setFirstName("Natasha");
		cust2.setLastName("Romanoff");
		cust2.setContactNo("93847563");
		cust2.setEmail("nr@gmail.com");
		cust2.setUserID("natrom");
		cust2.setPassword(passwordEncoder.encode("customer2"));
		cust2.setUserRole("user");
		cust2.setAddress(new Address());

		customerRepository.save(cust2);

	}

}
