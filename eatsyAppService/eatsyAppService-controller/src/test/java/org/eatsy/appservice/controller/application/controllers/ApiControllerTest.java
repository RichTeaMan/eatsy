package org.eatsy.appservice.controller.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.service.RecipeFactory;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParamters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API Controller unit tests with Mockito.
 */
//These two annotations tell Mockito to create the mocks based on the @Mock annotation and enable autowired
@SpringBootTest
@AutoConfigureMockMvc
//Define lifecycle of tests to be per class rather than per class. Allows use of @BeforeAll.
//Responses are mocked so PER_METHOD is not needed
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiControllerTest {

    //mockMvc auto-configured and part of the dependencies directly loaded for this test class
    @Autowired
    private MockMvc mockMvc;

    //ObjectMapper auto-configured and part of the dependencies directly loaded for this test class
    @Autowired
    private ObjectMapper objectMapper;

    //Tells Mockito to mock the RecipeFactory instance
    @MockBean
    private RecipeFactory recipeFactoryHandler;

    /**
     * Test the add recipe endpoint
     */
    @Test
    public void checkAddRecipeSuccess() {

        //Setup - create a recipeModel object for mocking the RecipeFactory service whilst the /add endpoint
        // (in the REST controller) is under test.
        final RecipeModel recipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);

        //Executes some code of the class under test. In this case, build the mock request that will hit the
        // "/add" endpoint and trigger the below chain method.
        MockHttpServletRequestBuilder mockRequest;
        try {
            mockRequest = MockMvcRequestBuilders.post(EatsyRecipeTestParamters.ADD_RECIPE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(recipeModel));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //When the created recipe is returned it will have a UUID - update this for mock response.
        final RecipeModel recipeModelToReturn = new RecipeModel();
        recipeModelToReturn.setKey(UUID.randomUUID().toString());
        recipeModelToReturn.setName(recipeModel.getName());
        recipeModelToReturn.setIngredientSet(recipeModel.getIngredientSet());
        recipeModelToReturn.setMethod(recipeModel.getMethod());

        //Configure the mock to return the recipeModel when the createRecipeModel is called.
        //This chain method mocks the createRecipe() method call in the RecipeFactory, so every time the method is
        // called within the controller (triggered later in this test), it will return the specified value
        // in the parameter of the thenReturn() method.
        // In this case it returns the pre-set recipeModel (defined in this test setup), instead of actually calling
        // the createRecipe() method in the RecipeFactory service.
        Mockito.when(recipeFactoryHandler.createRecipe(recipeModel)).thenReturn(recipeModelToReturn);

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.ingredientSet").exists());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test the retrieve all recipes endpoint
     */
    @Test
    public void checkRetrieveAllRecipesSuccess() {

        //Create a list of recipes to return in the mock;
        List<RecipeModel> allRecipes = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParamters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        //Gather some information about the data to validate the assertion
        String nameOfFirstRecipeInList = allRecipes.get(0).getName();

        //Configure the mock to return the recipes when the retrieveAllRecipes is called.
        Mockito.when(recipeFactoryHandler.retrieveAllRecipes()).thenReturn(allRecipes);

        //Build the mock request that will hit the "/retrieveAllRecipes" endpoint and trigger the above chain method.
        MockHttpServletRequestBuilder mockRequest;
        try {
            mockRequest = MockMvcRequestBuilders.get(EatsyRecipeTestParamters.RETRIEVE_ALL_RECIPES)
                    .contentType(MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(allRecipes.size())))
                    .andExpect(jsonPath("$[0].name", is(nameOfFirstRecipeInList)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test the delete recipe endpoint.
     */
    @Test
    public void checkDeleteRecipeEndpointSuccess() {

        //Create a list of recipes to return in the mock;
        List<RecipeModel> allRecipes = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParamters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        //Gather some information about the data to validate the assertion
        String nameOfFirstRecipeInList = allRecipes.get(0).getName();

        /*
        Configure the mock to return the recipe model list when the deleteRecipe endpoint is called.
        None of the created models in this test have keys assigned. They are not needed as the response is mocked.
        This test confirms the endpoint is correctly hit and service method triggered. If the test was interacting with the service layer,
        it would fail due to not finding a recipeModel with the corresponding key (this test case is covered in the service layer unit tests.
         */
        String key = UUID.randomUUID().toString();
        Mockito.when(recipeFactoryHandler.deleteRecipe(key)).thenReturn(allRecipes);

        //Build the mock request that will hit the "/deleteRecipe" endpoint and trigger the above chain method.
        MockHttpServletRequestBuilder mockRequest;
        try {
            mockRequest = MockMvcRequestBuilders.delete(EatsyRecipeTestParamters.DELETE_RECIPE, key)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(allRecipes.size())))
                    .andExpect(jsonPath("$[0].name", is(nameOfFirstRecipeInList)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Test the edit recipe endpoint.
     */
    @Test
    public void checkEditRecipeEndpointSuccess() {

        //Setup
        //Create two recipes in the list of recipes
        List<RecipeModel> allRecipes = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParamters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);

        //Create an 'updated version' of one of the two recipes by submitting an edit to the name.
        RecipeModel updatedRecipe = allRecipes.get(0);
        String updatedRecipeName = "Updated name";
        updatedRecipe.setKey(UUID.randomUUID().toString()); //The stored recipe would have a key, so this makes the mock response more realistic.
        updatedRecipe.setName(updatedRecipeName);

        //Configure the mock to return the updated recipe when the edit endpoint is called.
        Mockito.when(recipeFactoryHandler.updateRecipe(updatedRecipe.getKey(), updatedRecipe)).thenReturn(updatedRecipe);

        //Build the mock request that will hit the "/edit/{recipeKey}" endpoint and trigger the above chain method.
        MockHttpServletRequestBuilder mockRequest;
        String recipeKey = updatedRecipe.getKey();
        try {
            mockRequest = MockMvcRequestBuilders.put(EatsyRecipeTestParamters.EDIT_RECIPE + recipeKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(updatedRecipe));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Execute the test and assert the response is as expected.
        try {
            mockMvc.perform(mockRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(updatedRecipeName)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
