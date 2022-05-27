package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactoryHandler;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParamters;
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
        maxNumberOfRecipes = EatsyRecipeTestParamters.MAX_NUMBER_OF_RECIPES;
        maxIngredientSetSize = EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE;
        maxMethodMapSize = EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE;
    }

    /**
     * Check the recipe factory correctly deletes the specified recipe
     * and returns the updated list of all recipes
     */
    @Test
    public void checkDeleteAllRecipes() {

        //Setup
        //Create a list of recipes to be added to the recipeCache
        List<RecipeModel> expectedRecipeModelList = recipeModelDataFactory.generateRecipeModelsList(maxNumberOfRecipes, maxIngredientSetSize, maxMethodMapSize);

        //Populate the recipeCache with the randomly generated recipe list
        for (RecipeModel currentRecipeModel : expectedRecipeModelList) {
            RecipeModel createdRecipeModel = recipeFactory.createRecipe(currentRecipeModel);

            //To ensure the test doesn't fail on the randomly generated UUIDs when doing object comparisons.
            currentRecipeModel.setKey(createdRecipeModel.getKey());
        }

        //Test - delete one of the recipes in the recipeCache
        //Select a recipe in the list for deletion at random
        int randomListIndex = (int) ((Math.random() * expectedRecipeModelList.size()));
        String uniqueKeyOfRecipeToDelete = expectedRecipeModelList.get(randomListIndex).getKey();
        RecipeModel recipeModelToBeDeleted = expectedRecipeModelList.get(randomListIndex);
        //Delete the recipe
        List<RecipeModel> actualUpdatedRecipeModelList = recipeFactory.deleteRecipe(uniqueKeyOfRecipeToDelete);

        //Assert - check the recipe requested for deletion is no longer in the list of recipes, Check all the others are.
        Assertions.assertTrue(expectedRecipeModelList.containsAll(actualUpdatedRecipeModelList));
        Assertions.assertTrue((expectedRecipeModelList.size() - 1) == actualUpdatedRecipeModelList.size());
        Assertions.assertTrue(!actualUpdatedRecipeModelList.contains(recipeModelToBeDeleted));//check the recipe to be deleted is in-fact gone.
    }

}
