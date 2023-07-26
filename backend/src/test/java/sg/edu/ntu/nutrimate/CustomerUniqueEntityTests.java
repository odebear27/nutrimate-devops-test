package sg.edu.ntu.nutrimate;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.ntu.nutrimate.controller.CustomerController;
import sg.edu.ntu.nutrimate.entity.Address;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.exception.CustomerEntityNotUniqueException;
import sg.edu.ntu.nutrimate.service.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc // to configure mockMVC
public class CustomerUniqueEntityTests {

    @Autowired
	MockMvc mockMvc;

    @Mock // To mock the service layer
	CustomerService mockService;

    @InjectMocks
    CustomerController customerController;

    // @Autowired
	// private ObjectMapper objectMapper; //convert java objects to json and vice verse

    // @Test
	// public void CustomerIsUnique() throws Exception {
	// 	// Arrange - Mock the data
	// 	Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
	// 	Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "natrom", "widowblackout", address );

    //     // when(mockRepo.findByUserID("natrom")).thenReturn(Optional.empty());
	// 	// when(mockService.createCustomerAccount(newCustomer)).thenReturn(newCustomer);
        
	// 	// Act and Assert
	// 	assertEquals(new ResponseEntity<>(HttpStatus.CREATED), customerController.createCustomerAccount(newCustomer));		
	// }

	@Test
	public void CustomerIsNotUnique() throws Exception {
		// Arrange - Mock the data
		Address address = new Address("123", "boardway", "12", "Stark Tower", "123456");
		Customer newCustomer = new Customer(3, "Natasha", "romanoff", "blackwin@avenger.com", "12345678", "natrom", "widowblackout", "user", address );

        when(mockService.createCustomerAccount(newCustomer)).thenThrow(new CustomerEntityNotUniqueException("natrom"));
        					
		// Act and Assert
		// assertThatThrownBy(
		// 	() -> customerController.createCustomerAccount(newCustomer))
		// 	.hasCauseInstanceOf(CustomerUserIDNotUniqueException.class)
		// 	.hasMessage("Customer User ID natrom is not unique");

		Throwable thrown = catchThrowable(() -> customerController.createCustomerAccount(newCustomer));
		assertThat(thrown).as("Customer User ID natrom is not unique").isInstanceOf(CustomerEntityNotUniqueException.class);
	}
    
}
