package sg.edu.ntu.nutrimate.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// This ApiResponse class is used to map the JSON response from the Spoonacular API into a List of ApiRecipe
// ignore all other properties that are not defined in this class
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private List<ApiRecipe> results;

    public List<ApiRecipe> getResults() {
        return this.results;
    }

    public void setResults(List<ApiRecipe> results) {
        this.results = results;
    }

}
