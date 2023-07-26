package sg.edu.ntu.nutrimate;

// import com.github.tomakehurst.wiremock.WireMockServer;
// import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import sg.edu.ntu.nutrimate.controller.RecipeController;
import sg.edu.ntu.nutrimate.entity.ApiRecipe;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.entity.Recipe;
import sg.edu.ntu.nutrimate.repository.RecipeRepository;
import sg.edu.ntu.nutrimate.service.RecipeService;
import sg.edu.ntu.nutrimate.service.SpoonacularService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// @SpringBootTest(classes = { RecipeController.class, RecipeService.class })
@SpringBootTest
@TestPropertySource(locations = "/test.properties")
public class RecipeControllerTest {

        // private WireMockServer wireMockServer;

        @Autowired
        private RecipeController recipeController;

        @MockBean
        private RecipeService recipeService;

        @MockBean
        private SpoonacularService spoonacularService;

        @MockBean
        private RecipeRepository recipeRepository;

        // Test for searching for recipes by name and cuisine
        // Testing the object mapping of the response
        // Testing to combine ApiRecipe with CustomerRecipe
        @Test
        public void testSearchForRecipes() {
                // Define the mock HTTP response
                String recipeJsonResponse = "["
                                + "{"
                                + "\"id\":1,"
                                + "\"sourceUrl\":\"www.spoonacular.recipe1.com\","
                                + "\"spoonacularSourceUrl\":\"www.recipe1.com\","
                                + "\"cuisines\":[\"Italian\"],"
                                + "\"image\":\"www.image.recipe1.com\","
                                + "\"readyInMinutes\":30,"
                                + "\"title\":\"Hawaiian Pizza\","
                                + "\"summary\":\"A favourite among children\""
                                + "},"
                                + "{"
                                + "\"id\":2,"
                                + "\"sourceUrl\":\"www.spoonacular.recipe2.com\","
                                + "\"spoonacularSourceUrl\":\"www.recipe2.com\","
                                + "\"cuisines\":[\"Italian\"],"
                                + "\"image\":\"www.image.recipe2.com\","
                                + "\"readyInMinutes\":30,"
                                + "\"title\":\"Pepperoni Pizza\","
                                + "\"summary\":\"Perfect for meat lovers\""
                                + "}"
                                + "]";

                // Arrange
                ApiRecipe apiRecipe = new ApiRecipe(
                                1,
                                "www.spoonacular.recipe1.com",
                                "www.recipe1.com",
                                Arrays.asList("Italian"),
                                "www.image.recipe1.com",
                                30,
                                "Hawaiian Pizza",
                                "A favourite among children");

                CustomerRecipe customerRecipe = new CustomerRecipe(2, "Margherita Pizza",
                                "A classic Italian pizza made with tomato sauce, mozzarella cheese, and basil leaves.",
                                30, "Italian", new Customer());

                // Create list for apiRecipes and customerRecipes
                List<ApiRecipe> apiRecipes = List.of(apiRecipe);
                List<CustomerRecipe> customerRecipes = List.of(customerRecipe);

                // Combine the two lists
                List<Recipe> combinedRecipes = new ArrayList<>();
                combinedRecipes.addAll(customerRecipes);
                combinedRecipes.addAll(apiRecipes);

                // Mock the call to the service
                when(recipeService.searchRecipes(anyString()))
                                .thenReturn(combinedRecipes);

                // Mock the calls to Spoonacular service and recipe repository
                // these are inside the recipeService searchRecipes method
                when(spoonacularService.getRecipes(anyString())).thenReturn(recipeJsonResponse);
                when(recipeRepository.findByTitleContaining(anyString())).thenReturn(customerRecipes);

                // Call the method
                // For this test, the search term need not be what the title contains
                // Testing for object mapping instead
                ResponseEntity<List<Recipe>> responseEntity = recipeController.getAllRecipes(anyString());

                // Verify the results
                assertEquals(200, responseEntity.getStatusCodeValue());
                List<Recipe> recipes = responseEntity.getBody();
                assertEquals(2, recipes.size());

                // Print out the titles of the recipes
                System.out.println(((CustomerRecipe) recipes.get(0)).getTitle());
                System.out.println(((ApiRecipe) recipes.get(1)).getTitle());

                assertEquals("Margherita Pizza", ((CustomerRecipe) recipes.get(0)).getTitle());
                assertEquals("Hawaiian Pizza", ((ApiRecipe) recipes.get(1)).getTitle());
        }
}
