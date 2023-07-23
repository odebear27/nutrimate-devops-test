package sg.edu.ntu.nutrimate.service;

import java.io.*;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SpoonacularService {

    @Value("${spoonacular.api.key}")
    private String API_KEY;

    public String getRecipes(String query) {
        String result = "";

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/spoonacular/recipes/complexSearch?query=" + query
                        + "&addRecipeInformation=true&number=3")
                .header("apikey", API_KEY)
                .method("GET", null)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);

            result = response.body().string();
            // System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
