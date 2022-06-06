package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.eatsy.appservice.persistence.service.EatsyRepositoryHandler;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

/**
 * Recipe Factory unit tests for the Delete Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DeleteRecipeTests {

    /**
     * Class under test.
     */
    private RecipeFactory recipeFactory;


    @BeforeEach
    public void setup() {
        //RecipeFactory dependent on RecipeMapper and EatsyRepositoryHandler
        final RecipeMapper recipeMapper = new RecipeMapperHandler();
        final EatsyRepositoryService eatsyRepositoryService = new EatsyRepositoryHandler();
        recipeFactory = new RecipeFactoryHandler(recipeMapper, eatsyRepositoryService);

    }

    /**
     * Check the recipe factory correctly deletes the specified recipe
     * and returns the updated list of all recipes
     */
    @Test
    public void checkDeleteAllRecipes() {

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

        //Test - delete one of the recipes in the recipeCache
        //Select a recipe in the list for deletion at random
        final int randomListIndex = (int) ((Math.random() * expectedRecipeModelList.size()));
        final String uniqueKeyOfRecipeToDelete = expectedRecipeModelList.get(randomListIndex).getKey();
        final RecipeModel recipeModelToBeDeleted = expectedRecipeModelList.get(randomListIndex);
        //Delete the recipe
        final List<RecipeModel> actualUpdatedRecipeModelList = recipeFactory.deleteRecipe(uniqueKeyOfRecipeToDelete);

        //Assert - check the recipe requested for deletion is no longer in the list of recipes, Check all the others are.
        Assertions.assertTrue(expectedRecipeModelList.containsAll(actualUpdatedRecipeModelList));
        Assertions.assertEquals((expectedRecipeModelList.size() - 1), actualUpdatedRecipeModelList.size());
        Assertions.assertFalse(actualUpdatedRecipeModelList.contains(recipeModelToBeDeleted));//check the recipe to be deleted is in-fact gone.
    }

}
