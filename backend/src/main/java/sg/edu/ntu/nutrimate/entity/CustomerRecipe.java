package sg.edu.ntu.nutrimate.entity;

// import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
// import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Table(name = "recipe")
public class CustomerRecipe extends Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private int recipeId;

    @Column(name = "recipe_name")
    @NotBlank(message = "Recipe title is mandatory")
    @Size(max = 50, message = "Recipe title should be less than 50 characters")
    private String title;

    @Column(name = "recipe_description")
    @Size(max = 1000, message = "Recipe summary should be less than 1000 characters")
    private String summary;

    @Column(name = "ready_in_minutes")
    @PositiveOrZero(message = "Ready in minutes should be a positive number")
    private int readyInMinutes;

    @Column(name = "cuisines")
    private String cuisines;

    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "recipeImage_id", referencedColumnName = "recipeImage_id")
    // private RecipeImage recipeImage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", referencedColumnName = "userID")
    private Customer customer;

    public CustomerRecipe() {
    }

    public CustomerRecipe(int recipeId, String title, String summary, int readyInMinutes, String cuisines,
            Customer customer) {
        this.recipeId = recipeId;
        this.title = title;
        this.summary = summary;
        this.readyInMinutes = readyInMinutes;
        this.cuisines = cuisines;
        this.customer = customer;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // public RecipeImage getRecipeImage() {
    // return this.recipeImage;
    // }

    // public void setRecipeImage(RecipeImage recipeImage) {
    // this.recipeImage = recipeImage;
    // }

    public int getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getReadyInMinutes() {
        return this.readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public String getCuisines() {
        return this.cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

}
