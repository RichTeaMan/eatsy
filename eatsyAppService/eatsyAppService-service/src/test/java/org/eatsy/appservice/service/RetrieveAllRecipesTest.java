package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactoryHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

/**
 * Recipe Factory unit tests for the Create Recipe Method
 */
//Define lifecycle of tests to be per class rather than per method default. Allows use of @BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RetrieveAllRecipesTest {

    /**
     * Class under test.
     */
    private RecipeFactory recipeFactory;

    //RecipeFactory dependent on RecipeMapper
    private RecipeMapper recipeMapper;

    //Factory where the data generation methods are stored
    private RecipeModelDataFactory recipeModelDataFactory;

    //Max value for the generated number of ingredients in the recipe
    private int maxIngredientSetSize;

    //Max value for the generated number of method steps in the recipe
    private int maxMethodMapSize;

    //Max value for the generated number of recipeModels in the recipeModel list.
    private int maxNumberOfRecipes;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
        recipeFactory = new RecipeFactoryHandler(recipeMapper);
        recipeModelDataFactory = new RecipeModelDataFactoryHandler();
        maxNumberOfRecipes = 15;
        maxIngredientSetSize = 20;
        maxMethodMapSize = 10;
    }

    /**
     * Check the recipe factory correctly returns all recipes
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup
        //Populate the recipeCache with recipes
        List<RecipeModel> expectedRecipeModelList = recipeModelDataFactory.generateRecipeModelsList(maxNumberOfRecipes, maxIngredientSetSize, maxMethodMapSize);

        //Populate the recipeCache with the randomly generated recipes
        for (RecipeModel currentRecipeModel : expectedRecipeModelList) {
            RecipeModel createdRecipeModel = recipeFactory.createRecipe(currentRecipeModel);

            //To ensure the test doesn't fail on the randomly generated UUIDs.
            currentRecipeModel.setKey(createdRecipeModel.getKey());
        }

        //Test
        List<RecipeModel> actualRecipeModelsList = recipeFactory.retrieveAllRecipes();

        //Assert
        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }

}
