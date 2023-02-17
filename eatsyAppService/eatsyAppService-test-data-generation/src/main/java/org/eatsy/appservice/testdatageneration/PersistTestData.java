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
 * Used to generate test data on demand and persist it for manual test/validation purposed.
 */
public class PersistTestData {

    // Main method acts as the entry point from the gradle task defined in the root gradle project
    public static void main(final String[] args) throws IOException {
        generateTestData();
    }

    //Generate test data and post it to the addRecipe endpoint in the controller
    public static void generateTestData() throws IOException {
        final RecipeModel recipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        final String postUrl = "http://localhost:8080/";
        final Gson gson = new Gson();
        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(postUrl);
        //Convert POJO to JSON
        final StringEntity postingString = new StringEntity(gson.toJson(recipeModel));
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        final HttpResponse response = httpClient.execute(post);
    }
}
