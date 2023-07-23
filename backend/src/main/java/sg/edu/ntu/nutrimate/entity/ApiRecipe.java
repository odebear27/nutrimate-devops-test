package sg.edu.ntu.nutrimate.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// ignore all other properties that are not defined in this class
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiRecipe extends Recipe {

    private int id;
    private String image;
    private String spoonacularSourceUrl;
    private String sourceUrl;
    private List<String> cuisines;

    public ApiRecipe() {
    }

    public ApiRecipe(int id, String spoonacularSourceUrl, String sourceUrl, List<String> cuisines, String image,
            int readyInMinutes, String title, String summary) {
        this.id = id;
        this.spoonacularSourceUrl = spoonacularSourceUrl;
        this.sourceUrl = sourceUrl;
        this.cuisines = cuisines;
        this.image = image;
        this.setReadyInMinutes(readyInMinutes);
        this.setTitle(title);
        this.setSummary(summary);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpoonacularSourceUrl() {
        return this.spoonacularSourceUrl;
    }

    public void setSpoonacularSourceUrl(String spoonacularSourceUrl) {
        this.spoonacularSourceUrl = spoonacularSourceUrl;
    }

    public String getSourceUrl() {
        return this.sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public List<String> getCuisines() {
        return this.cuisines;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

}
