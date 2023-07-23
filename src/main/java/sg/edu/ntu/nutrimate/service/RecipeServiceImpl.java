package sg.edu.ntu.nutrimate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.ntu.nutrimate.entity.ApiRecipe;
import sg.edu.ntu.nutrimate.entity.ApiResponse;
import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.entity.Recipe;
import sg.edu.ntu.nutrimate.exception.CustomerNoPermissionException;
import sg.edu.ntu.nutrimate.exception.CustomerNotFoundException;
import sg.edu.ntu.nutrimate.exception.RecipeNotFoundException;
import sg.edu.ntu.nutrimate.repository.CustomerRepository;
import sg.edu.ntu.nutrimate.repository.RecipeRepository;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    private CustomerRepository customerRepository;

    @Autowired
    private SpoonacularService spoonacularService;

    public RecipeServiceImpl(RecipeRepository recipeRepository, CustomerRepository customerRepository) {
        this.recipeRepository = recipeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerRecipe uploadCustomerRecipe(String userID, CustomerRecipe recipe) {
        Optional<Customer> wrappedSelectedCustomer = customerRepository.findByUserID(userID);

        if (!wrappedSelectedCustomer.isPresent()) {
            throw new CustomerNotFoundException(userID);
        }

        Customer selectedCustomer = wrappedSelectedCustomer.get();
        recipe.setCustomer(selectedCustomer);
        return recipeRepository.save(recipe);
    }

    @Override
    public List<CustomerRecipe> getAllCustomerRecipesByUserID(String userID) {

        Optional<Customer> wrappedSelectedCustomer = customerRepository.findByUserID(userID);

        if (!wrappedSelectedCustomer.isPresent()) {
            throw new CustomerNotFoundException(userID);
        }
        Customer customer = wrappedSelectedCustomer.get();
        List<CustomerRecipe> customerRecipes = recipeRepository.findByCustomer(customer);

        return customerRecipes;
    }

    @Override
    public CustomerRecipe updateRecipe(int recipeId, String userID,
            CustomerRecipe recipe) {
        Optional<Customer> wrappedSelectedCustomer = customerRepository.findByUserID(userID);
        CustomerRecipe recipeToUpdate = recipeRepository.findById(recipeId).get();

        if (!wrappedSelectedCustomer.isPresent()) {
            throw new CustomerNotFoundException(userID);
        }

        Customer selectedCustomer = wrappedSelectedCustomer.get();

        if (selectedCustomer.getUserID().equals(recipeToUpdate.getCustomer().getUserID())) {

            recipeToUpdate.setTitle(recipe.getTitle());
            recipeToUpdate.setSummary(recipe.getSummary());
            recipeToUpdate.setReadyInMinutes(recipe.getReadyInMinutes());
            recipeToUpdate.setCuisines(recipe.getCuisines());

            return recipeRepository.save(recipeToUpdate);
        } else {
            throw new CustomerNoPermissionException("You do not have permission to update this recipe");
        }

    }

    @Override
    public void deleteRecipe(int recipeId, String userID) {
        Optional<Customer> wrappedSelectedCustomer = customerRepository.findByUserID(userID);
        CustomerRecipe recipe = recipeRepository.findById(recipeId).get();

        if (!wrappedSelectedCustomer.isPresent()) {
            throw new CustomerNotFoundException(userID);
        }

        Customer selectedCustomer = wrappedSelectedCustomer.get();

        if (recipe.getCustomer().getUserID().equals(selectedCustomer.getUserID())) {
            recipeRepository.deleteById(recipeId);
        } else {
            throw new CustomerNoPermissionException("You do not have permission to delete this recipe");
        }
    }

    @Override
    public List<Recipe> searchRecipes(String search) {
        List<CustomerRecipe> customerRecipes = new ArrayList<>();
        customerRecipes = recipeRepository.findByTitleContaining(search);
        String recipeData = spoonacularService.getRecipes(search);

        System.out.println(customerRecipes);
        System.out.println(recipeData);

        List<ApiRecipe> apiRecipes = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiResponse response = mapper.readValue(recipeData, ApiResponse.class);

            apiRecipes = response.getResults();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // return apiRecipes;
        }

        // combine the two lists together
        List<Recipe> combinedRecipes = new ArrayList<>();
        combinedRecipes.addAll(customerRecipes);
        combinedRecipes.addAll(apiRecipes);

        if (combinedRecipes.isEmpty()) {
            throw new RecipeNotFoundException();
        }
        return combinedRecipes;
    }

    @Override
    public List<Recipe> searchRecipesByCuisine(String cuisine) {
        List<Recipe> customerRecipes = new ArrayList<>();
        customerRecipes = recipeRepository.findByCuisinesIgnoreCase(cuisine);
        String recipeData = spoonacularService.getRecipes(cuisine);

        // System.out.println(customerRecipes);
        // System.out.println(recipeData);

        List<ApiRecipe> apiRecipes = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiResponse response = mapper.readValue(recipeData, ApiResponse.class);

            apiRecipes = response.getResults();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // return apiRecipes;
        }

        // combine the two lists together
        List<Recipe> combinedRecipes = new ArrayList<>();
        combinedRecipes.addAll(customerRecipes);
        combinedRecipes.addAll(apiRecipes);

        if (combinedRecipes.isEmpty()) {
            throw new RecipeNotFoundException();
        }
        return combinedRecipes;

    }

    @Override
    public List<CustomerRecipe> adminGetAllCustomerRecipes() {
        List<CustomerRecipe> allRecipes = recipeRepository.findAll();
        return allRecipes;
    }

    @Override
    public CustomerRecipe adminGetCustomerRecipeById(int recipeId) {
        Optional<CustomerRecipe> customerRecipe = recipeRepository.findById(recipeId);
        if (!customerRecipe.isPresent()) {
            throw new RecipeNotFoundException();
        }
        return customerRecipe.get();
    }

    @Override
    public void adminDeleteRecipe(int recipeId) {

        try {
            recipeRepository.deleteById(recipeId);
        } catch (Exception e) {
            throw new RecipeNotFoundException();
        }
    }

}
