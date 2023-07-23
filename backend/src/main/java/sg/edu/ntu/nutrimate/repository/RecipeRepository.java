package sg.edu.ntu.nutrimate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.ntu.nutrimate.entity.Customer;
import sg.edu.ntu.nutrimate.entity.CustomerRecipe;
import sg.edu.ntu.nutrimate.entity.Recipe;

public interface RecipeRepository extends JpaRepository<CustomerRecipe, Integer> {

    // select * from recipe where title like '%item%'
    // % means any number of characters
    // item is the placeholder
    @Query("SELECT r FROM CustomerRecipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :item, '%'))")
    List<CustomerRecipe> findByTitleContaining(@Param("item") String item);

    List<Recipe> findByCuisinesIgnoreCase(@Param("cuisine") String cuisine);

    List<CustomerRecipe> findByCustomer(@Param("customer") Customer customer);

    Optional<CustomerRecipe> findByRecipeId(int recipeId);
  
    List<CustomerRecipe> findAllByCustomer(@Param("customer") Customer customer);

    void deleteAllByCustomer(Customer customer);
}
