package sg.edu.ntu.nutrimate.service;

import java.util.List;

import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.entity.Recipe;

public interface RecipeService {

    CustomerRecipe uploadCustomerRecipe(String userID, CustomerRecipe recipe);

    public List<CustomerRecipe> getAllCustomerRecipesByUserID(String userID);

    public CustomerRecipe updateRecipe(int recipeId, String userID,
            CustomerRecipe recipe);

    public void deleteRecipe(int recipeId, String userID);

    public List<Recipe> searchRecipes(String search);

    public List<Recipe> searchRecipesByCuisine(String cuisine);

    public List<CustomerRecipe> adminGetAllCustomerRecipes();

    public CustomerRecipe adminGetCustomerRecipeById(int recipeId);

    public void adminDeleteRecipe(int recipeId);

}
