package org.eatsy.appservice.testdatageneration;


import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.annotations.Generated;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;

import java.io.IOException;
import java.util.List;

import static org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters.*;

/**
 * Class to persist test data through the controller layer into the database.
 * Used to generate test data on demand and persist it for manual test/validation purposes.
 * This is executed via the gradle task "generateRandomRecipes"
 */
@Generated
public class GenerateAndPersistRandomRecipes {

    // Main method acts as the entry point from the gradle task defined in the root gradle project
    public static void main(final String[] args) throws IOException {
        persistRandomRecipes();
    }

    /**
     * Creates a list of random RecipeModel objects and persists them by using the local deployment of the Eatsy-API.
     * The number of recipes creates is a random number between 1 and 15
     *
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

            // Create a MultipartEntityBuilder to build the multipart request body
            final MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            // Convert the RecipeModel instance to JSON format and add it as a part
            entityBuilder.addTextBody("recipeModel", gson.toJson(currentRecipeModel), ContentType.APPLICATION_JSON);

            // Add images to the multipart request
            for (int i = 0; i < 3; i++) {
                // Download the image from the URL
                final byte[] imageData = downloadImage("https://source.unsplash.com/random/?food");
                if (imageData != null) {
                    // Add the image data as a binary body part
                    entityBuilder.addBinaryBody("images", imageData, ContentType.DEFAULT_BINARY, "image"+i+".jpg");
                }
            }

            // Build the multipart request entity
            final HttpEntity multipartEntity = entityBuilder.build();
            // Set the multipart request entity as the content of the HTTP POST request
            post.setEntity(multipartEntity);

            // Send the HTTP POST request to the addRecipe endpoint and receive the HTTP response
            final HttpResponse response = httpClient.execute(post);

            // Read the response
            final String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println(responseBody);
        }
    }

    private static byte[] downloadImage(final String imageUrl) throws IOException {
        // Create a HttpClient instance to send an HTTP GET request to download the image
        final HttpClient httpClient = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(imageUrl);

        // Send the HTTP GET request to download the image and receive the HTTP response
        final HttpResponse response = httpClient.execute(get);

        //Define byteArray to store the image
        byte[] imageAsByteArray = null;
        // Read the image content
        final HttpEntity imageEntity = response.getEntity();
        if (imageEntity != null) {
            imageAsByteArray = EntityUtils.toByteArray(imageEntity);
        } //TODO throw exception?

        return imageAsByteArray;

    }
}
