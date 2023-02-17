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
import java.util.List;

import static org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters.*;

/**
 * Class to persist test data through the controller layer into the database.
 * Used to generate test data on demand and persist it for manual test/validation purposes.
 * This is executed via the gradle task "generateRandomRecipes"
 */
public class GenerateAndPersistRandomRecipes {

    // Main method acts as the entry point from the gradle task defined in the root gradle project
    public static void main(final String[] args) throws IOException {
        persistRandomRecipes();
    }

    /**
     * Creates a list of random RecipeModel objects and persists them by using the local deployment of the Eatsy-API.
     * The number of recipes creates is a random number between 1 and 15
     * @throws IOException
     */
    public static void persistRandomRecipes() throws IOException {

        //Create a list of Recipe Model objects
        final List<RecipeModel> recipeModelList = RecipeModelDataFactory
                .generateRecipeModelsList(MAX_NUMBER_OF_RECIPES, MAX_INGREDIENT_SET_SIZE, MAX_METHOD_MAP_SIZE);

        for (final RecipeModel currentRecipeModel : recipeModelList) {

            // Create a Gson instance to convert the RecipeModel instance to JSON format
            final Gson gson = new Gson();
            // Create a HttpClient instance to send an HTTP POST request to the addRecipe endpoint
            final HttpClient httpClient = HttpClientBuilder.create().build();
            // Create an HttpPost instance to send an HTTP POST request to the addRecipe endpoint
            final HttpPost post = new HttpPost(EatsyRecipeTestParameters.ADD_RECIPE_TO_LOCAL_INSTANCE);
            // Convert the RecipeModel instance to JSON format
            final StringEntity postingString = new StringEntity(gson.toJson(currentRecipeModel));
            // Set the JSON content as the content of the HTTP POST request
            post.setEntity(postingString);
            // Set the content type of the HTTP POST request as JSON
            post.setHeader("Content-type", "application/json");
            // Send the HTTP POST request to the addRecipe endpoint and receive the HTTP response
            final HttpResponse response = httpClient.execute(post);
        }
    }
}
