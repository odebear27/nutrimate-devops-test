package sg.edu.ntu.nutrimate.controller;

import java.util.List;

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

import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.entity.Recipe;
import sg.edu.ntu.nutrimate.service.RecipeService;

@RestController
@RequestMapping("/nutrimate")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    // Add Recipe to Customer
    @PostMapping("/customers/{userID}/recipe")
    public ResponseEntity<CustomerRecipe> uploadCustomerRecipe(@PathVariable String userID,
            @Valid @RequestBody CustomerRecipe recipe) {

        CustomerRecipe newRecipe = recipeService.uploadCustomerRecipe(userID,
                recipe);

        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    // Get all recipes of a customer
    @GetMapping("/customers/{userID}/recipe")
    public ResponseEntity<List<CustomerRecipe>> getAllCustomerRecipesByUserID(@PathVariable String userID) {
        List<CustomerRecipe> allRecipes = recipeService.getAllCustomerRecipesByUserID(userID);
        return new ResponseEntity<>(allRecipes, HttpStatus.OK);
    }

    // Update Recipe
    @PutMapping("/customers/{userID}/recipe/{recipeId}")
    public ResponseEntity<CustomerRecipe> updateRecipe(@Valid @RequestBody CustomerRecipe recipe,
            @PathVariable int recipeId, @PathVariable String userID) {
        CustomerRecipe updatedRecipe = recipeService.updateRecipe(recipeId, userID,
                recipe);
        return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
    }

    // Delete Recipe
    @DeleteMapping("/customers/{userID}/recipe/{recipeId}")
    public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable int recipeId,
            @PathVariable String userID) {
        recipeService.deleteRecipe(recipeId, userID);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Search for Recipes
    @GetMapping("/public/recipe")
    public ResponseEntity<List<Recipe>> getAllRecipes(@RequestParam(required = false) String search) {

        List<Recipe> allRecipes;

        allRecipes = recipeService.searchRecipes(search);
        return new ResponseEntity<>(allRecipes, HttpStatus.OK);

    }

    // Search for Recipes by cuisine
    @GetMapping("/public/recipe/cuisine/{cuisine}")
    public ResponseEntity<List<Recipe>> searchByCuisine(@PathVariable String cuisine) {
        List<Recipe> allRecipes;

        allRecipes = recipeService.searchRecipesByCuisine(cuisine);
        return new ResponseEntity<>(allRecipes, HttpStatus.OK);
    }

    // Admin: get all customer recipes
    @GetMapping("/admin/recipe")
    public ResponseEntity<List<CustomerRecipe>> adminGetAllCustomerRecipes() {
        List<CustomerRecipe> allRecipes = recipeService.adminGetAllCustomerRecipes();
        return new ResponseEntity<>(allRecipes, HttpStatus.OK);
    }

    // Admin: get customer recipe by recipeId
    @GetMapping("/admin/recipe/{recipeId}")
    public ResponseEntity<CustomerRecipe> adminGetCustomerRecipeById(@PathVariable int recipeId) {
        CustomerRecipe customerRecipe = recipeService.adminGetCustomerRecipeById(recipeId);
        return new ResponseEntity<>(customerRecipe, HttpStatus.OK);
    }

    // Admin: delete customer recipe
    @DeleteMapping("/admin/recipe/{recipeId}")
    public ResponseEntity<HttpStatus> adminDeleteRecipe(@PathVariable int recipeId) {
        recipeService.adminDeleteRecipe(recipeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
