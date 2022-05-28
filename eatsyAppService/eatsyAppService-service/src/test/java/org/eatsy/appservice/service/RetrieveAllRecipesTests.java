package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

/**
 * Recipe Factory unit tests for the Create Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RetrieveAllRecipesTests {

    /**
     * Class under test.
     */
    private RecipeFactory recipeFactory;

    @BeforeEach
    public void setup() {
        //RecipeFactory dependent on RecipeMapper
        final RecipeMapper recipeMapper = new RecipeMapperHandler();
        recipeFactory = new RecipeFactoryHandler(recipeMapper);

    }

    /**
     * Check the recipe factory correctly returns all recipes
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup
        //Create the expected list of recipes to be retrieved
        final List<RecipeModel> expectedRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Populate the recipeCache with the randomly generated recipes
        for (final RecipeModel currentRecipeModel : expectedRecipeModelList) {
            final RecipeModel createdRecipeModel = recipeFactory.createRecipe(currentRecipeModel);

            //To ensure the test doesn't fail on the randomly generated UUIDs.
            currentRecipeModel.setKey(createdRecipeModel.getKey());
        }

        //Test
        final List<RecipeModel> actualRecipeModelsList = recipeFactory.retrieveAllRecipes();

        //Assert
        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }

}
