package org.eatsy.appservice.testdatageneration;


import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;

import java.io.IOException;

/**
 * Class to persist test data through the controller layer into the database.
 *
 * Used to generate test data on demand and persist it for manual test/validation purposes.
 * This is executed via the gradle task "generateRandomRecipes"
 */
public class GenerateAndPersistRandomRecipes {

    // Main method acts as the entry point from the gradle task defined in the root gradle project
    public static void main(final String[] args) throws IOException {
        persistRandomRecipes();
    }


    public static void persistRandomRecipes() throws IOException {
        final RecipeModel recipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        // Create a Gson instance to convert the RecipeModel instance to JSON format
        final Gson gson = new Gson();
        // Create a HttpClient instance to send an HTTP POST request to the addRecipe endpoint
        final HttpClient httpClient = HttpClientBuilder.create().build();
        // Create an HttpPost instance to send an HTTP POST request to the addRecipe endpoint
        final HttpPost post = new HttpPost(EatsyRecipeTestParameters.ADD_RECIPE_TO_LOCAL_INSTANCE);
        // Convert the RecipeModel instance to JSON format
        final StringEntity postingString = new StringEntity(gson.toJson(recipeModel));
        // Set the JSON content as the content of the HTTP POST request
        post.setEntity(postingString);
        // Set the content type of the HTTP POST request as JSON
        post.setHeader("Content-type", "application/json");
        // Send the HTTP POST request to the addRecipe endpoint and receive the HTTP response
        final HttpResponse response = httpClient.execute(post);
    }
}
