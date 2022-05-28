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
 * Recipe Factory unit tests for the Edit Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EditRecipeTests {

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
     * Check the Recipe Factory can edit an existing recipe.
     */
    @Test
    public void checkEditRecipeNameSuccess() {

        //Setup
        //Create a list of recipes to be added to the recipeCache
        final List<RecipeModel> expectedRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Populate the recipeCache with the randomly generated recipe list
        for (final RecipeModel currentRecipeModel : expectedRecipeModelList) {
            final RecipeModel createdRecipeModel = recipeFactory.createRecipe(currentRecipeModel);

            //To ensure the test doesn't fail on the randomly generated UUIDs when doing object comparisons.
            currentRecipeModel.setKey(createdRecipeModel.getKey());
        }

        //Get a new name to edit the recipe with
        final String newRecipeName = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE).getName();
        //Select a recipe in the list for the recipeName edit at random
        final int randomListIndex = (int) ((Math.random() * expectedRecipeModelList.size()));
        final String uniqueKeyOfRecipeToEdit = expectedRecipeModelList.get(randomListIndex).getKey();

        //Expected - What we expect the recipeModel to be after the name update (also what we will be submitting to the service as the updated model)
        final RecipeModel expectedUpdatedRecipeModel = expectedRecipeModelList.get(randomListIndex);
        expectedUpdatedRecipeModel.setName(newRecipeName);

        //Test - edit one of the recipe names in the recipeCache (This returned recipeModel will have a new key)
        final RecipeModel actualUpdatedRecipeModel = recipeFactory.updateRecipe(uniqueKeyOfRecipeToEdit, expectedUpdatedRecipeModel);
        //To ensure test doesn't fail on the randomly generated UUIDs
        expectedUpdatedRecipeModel.setKey(actualUpdatedRecipeModel.getKey());

        //Assert
        Assertions.assertEquals(expectedUpdatedRecipeModel, actualUpdatedRecipeModel);

    }

}
