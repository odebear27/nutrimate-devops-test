package sg.edu.ntu.nutrimate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import sg.edu.ntu.nutrimate.entity.Address;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.repository.RecipeRepository;
import sg.edu.ntu.nutrimate.service.RecipeServiceImpl;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks // Inject the mocks as dependencies
    RecipeServiceImpl recipeService;

    private Customer customer1;

    @BeforeEach
    public void setUp() throws Exception {
        Address address = new Address("123", "Hogwarts Drive", "01-01", "Hogwarts School", "123456");
        customer1 = new Customer(1, "Harry", "Potter", "harrypotter@hogwarts.com", "81234567", "harrypotter", "harrypw",
                "user",
                address);
        customerRepository.save(customer1);
    }

    @Test
    public void uploadRecipeTest() {

        // Arrange - mock the data
        CustomerRecipe newCustomerRecipe = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);

        when(customerRepository.findByUserID("harrypotter")).thenReturn(Optional.of(customer1));
        when(recipeRepository.save(newCustomerRecipe)).thenReturn(newCustomerRecipe);

        // Act
        CustomerRecipe savedCustomerRecipe = recipeService.uploadCustomerRecipe("harrypotter", newCustomerRecipe);

        // Assert
        verify(recipeRepository, times(1)).save(newCustomerRecipe);
        assertEquals(newCustomerRecipe, savedCustomerRecipe);
    }

    @Test
    public void updateRecipeTest() {

        // Arrange - mock the data
        CustomerRecipe existingCustomerRecipe = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);

        CustomerRecipe updatedCustomerRecipe = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);
        updatedCustomerRecipe.setReadyInMinutes(45);

        when(customerRepository.findByUserID("harrypotter")).thenReturn(Optional.of(customer1));
        when(recipeRepository.findById(1)).thenReturn(Optional.of(existingCustomerRecipe));
        when(recipeRepository.save(any(CustomerRecipe.class))).thenReturn(updatedCustomerRecipe);

        // Act
        CustomerRecipe savedCustomerRecipe = recipeService.updateRecipe(1, "harrypotter", updatedCustomerRecipe);

        // Assert
        verify(recipeRepository, times(1)).save(any(CustomerRecipe.class));
        assertEquals(updatedCustomerRecipe, savedCustomerRecipe);
    }

    @Test
    public void deleteRecipeTest() {
        // Arrange - mock the data
        CustomerRecipe existingCustomerRecipe = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);

        when(customerRepository.findByUserID("harrypotter")).thenReturn(Optional.of(customer1));
        when(recipeRepository.findById(1)).thenReturn(Optional.of(existingCustomerRecipe));

        // Act
        recipeService.deleteRecipe(1, "harrypotter");

        // Assert
        verify(recipeRepository, times(1)).deleteById(1);
    }

    @Test
    public void getAllCustomerRecipesByUserID() {
        // Arrange - mock the data
        CustomerRecipe recipe1 = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);

        when(customerRepository.findByUserID("harrypotter")).thenReturn(Optional.of(customer1));
        when(recipeRepository.findByCustomer(customer1)).thenReturn(Arrays.asList(recipe1));

        // Act
        List<CustomerRecipe> foundRecipes = recipeService.getAllCustomerRecipesByUserID("harrypotter");

        // Assert
        assertEquals(1, foundRecipes.size());
    }

    @Test
    public void adminGetAllCustomerRecipes() {
        // Arrange - mock the data
        CustomerRecipe recipe1 = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);

        when(recipeRepository.findAll()).thenReturn(Arrays.asList(recipe1));

        // Act
        List<CustomerRecipe> foundRecipes = recipeService.adminGetAllCustomerRecipes();

        // Assert
        assertEquals(1, foundRecipes.size());
    }

    @Test
    public void adminGetCustomerRecipeById() {
        // Arrange - mock the data
        CustomerRecipe recipe1 = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);

        when(recipeRepository.findById(1)).thenReturn(Optional.of(recipe1));

        // Act
        CustomerRecipe foundRecipe = recipeService.adminGetCustomerRecipeById(1);

        // Assert
        assertEquals(recipe1, foundRecipe);
    }

    @Test
    public void adminDeleteRecipe() {
        // Arrange - mock the data
        CustomerRecipe existingCustomerRecipe = new CustomerRecipe(1, "Chicken Rice",
                "Chicken rice is a Singapore local dish.", 30, "Asian", customer1);

        when(recipeRepository.findById(1)).thenReturn(Optional.of(existingCustomerRecipe));

        // Act
        recipeService.adminDeleteRecipe(1);

        // Assert
        verify(recipeRepository, times(1)).deleteById(1);
    }
}
