package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

/**
 * Recipe Factory unit tests
 */
//Define lifecycle of tests to be per class rather than per method default. Allows use of @BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RecipeFactoryTest {

    /**
     * Class under test.
     */
    private RecipeFactory recipeFactory;

    //RecipeFactory dependent on RecipeMapper
    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
        recipeFactory = new RecipeFactoryHandler(recipeMapper);
    }

    /**
     * Check the recipe factory correctly returns all recipes
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup

        //Populate the recipeCache with two recipes
        List<RecipeModel> expectedRecipeModelList = createRecipeModels();
        recipeFactory.createRecipe(expectedRecipeModelList.get(0));
        recipeFactory.createRecipe(expectedRecipeModelList.get(1));

        //Test

        List<RecipeModel> actualRecipeModelsList = recipeFactory.retrieveAllRecipes();
        //To ensure test doesn't fail on the randomly generated UUIDs
        expectedRecipeModelList.get(0).setKey(actualRecipeModelsList.get(0).getKey());
        expectedRecipeModelList.get(1).setKey(actualRecipeModelsList.get(1).getKey());

        //Assert
        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }

    /**
     * Check the recipe factory correctly deletes the specified recipe
     * and returns the updated list of all recipes
     */
    @Test
    public void checkDeleteRecipeEndpoint() {

        //Setup
        //Populate the recipeCache with two recipes
        List<RecipeModel> initialRecipeModelList = createRecipeModels();
        RecipeModel recipeModelOne = initialRecipeModelList.get(0);
        RecipeModel recipeModelTwo = initialRecipeModelList.get(1);
        RecipeModel responseRecipeModelOne = recipeFactory.createRecipe(recipeModelOne);
        RecipeModel responseRecipeModeTwo = recipeFactory.createRecipe(recipeModelTwo);

        //Expected - Use the responseRecipeModel object so the random generated key is populated
        List<RecipeModel> expectedRecipeModelList = new ArrayList<>();
        expectedRecipeModelList.add(responseRecipeModeTwo);

        //Test
        List<RecipeModel> actualUpdatedRecipeModelList = recipeFactory.deleteRecipe(responseRecipeModelOne.getKey());

        //Assert - check the recipe requested for deletion is no longer in the list of recipes
        Assertions.assertEquals(expectedRecipeModelList, actualUpdatedRecipeModelList);
    }

    /**
     * Check the Recipe Factory can edit an existing recipe.
     */
    @Test
    public void checkEditRecipeSuccess() {

        //Setup
        //Populate the recipeCache with two recipes
        List<RecipeModel> initialRecipeModelList = createRecipeModels();
        RecipeModel recipeModelOne = initialRecipeModelList.get(0);
        RecipeModel recipeModelTwo = initialRecipeModelList.get(1);
        RecipeModel responseRecipeModelOne = recipeFactory.createRecipe(recipeModelOne);
        RecipeModel responseRecipeModelTwo = recipeFactory.createRecipe(recipeModelTwo);

        //Expected - Update the name of the second recipe. It will also have a new key.
        RecipeModel updatedExpectedRecipeModel = responseRecipeModelTwo;
        updatedExpectedRecipeModel.setName("Updated name");

        //Test
        RecipeModel actualUpdatedRecipeModel = recipeFactory.updateRecipe(responseRecipeModelTwo.getKey(), updatedExpectedRecipeModel);

        //To ensure test doesn't fail on the randomly generated UUIDs
        updatedExpectedRecipeModel.setKey(actualUpdatedRecipeModel.getKey());
        //Assert - check the recipe list returned has replaced the original recipe object with the updated version
        Assertions.assertEquals(updatedExpectedRecipeModel, actualUpdatedRecipeModel);
    }

    /**
     * Create two recipe model objects. Expected result for the checkRetrieveAllRecipesSuccess test.
     */
    private List<RecipeModel> createRecipeModels() {
        String recipeName = "Ham and cheese panini";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Ham");
        ingredientSet.add("Cheese");
        ingredientSet.add("Panini");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "combine Ham and cheese in the panini");
        method.put(2, "heat panini to melt cheese");

        final RecipeModel inputRecipeModelOne = new RecipeModel();
        inputRecipeModelOne.setName(recipeName);
        inputRecipeModelOne.setIngredientSet(ingredientSet);
        inputRecipeModelOne.setMethod(method);

        String recipeNameTwo = "Jam and peanut butter sandwich";
        Set<String> ingredientSetTwo = new HashSet<>();
        ingredientSetTwo.add("Jam");
        ingredientSetTwo.add("Peanut butter");
        ingredientSetTwo.add("Sandwich");
        Map<Integer, String> methodTwo = new HashMap<>();
        methodTwo.put(1, "combine Jam and peanut butter in the sandwich");
        methodTwo.put(2, "Cut the crusts off");

        final RecipeModel inputRecipeModelTwo = new RecipeModel();
        inputRecipeModelTwo.setName(recipeNameTwo);
        inputRecipeModelTwo.setIngredientSet(ingredientSetTwo);
        inputRecipeModelTwo.setMethod(methodTwo);

        List<RecipeModel> recipeModelList = new ArrayList<>();
        recipeModelList.add(inputRecipeModelOne);
        recipeModelList.add(inputRecipeModelTwo);

        return recipeModelList;

    }

}
