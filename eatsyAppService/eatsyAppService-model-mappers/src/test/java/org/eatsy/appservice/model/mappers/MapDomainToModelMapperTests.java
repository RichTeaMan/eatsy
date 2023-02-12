package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.RecipeDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Recipe Map to Model Mapper unit tests
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MapDomainToModelMapperTests {

    /**
     * Class under test.
     */
    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
    }

    /**
     * This test checks the Recipe object is correctly mapped to a Recipe Model object.
     */
    @Test
    public void checkMapToModel() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipe.getKey();

        //Expectation
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipe.getName());
        expectedRecipeModel.setUploader(recipe.getUploader());
        expectedRecipeModel.setRecipeSummary(recipe.getRecipeSummary());
        expectedRecipeModel.setThumbsUpCount(recipe.getThumbsUpCount());
        expectedRecipeModel.setThumbsDownCount(recipe.getThumbsDownCount());
        expectedRecipeModel.setTags(recipe.getTags());
        expectedRecipeModel.setIngredients(recipe.getIngredients());
        expectedRecipeModel.setMethod(recipe.getMethod());

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapDomainToModel(recipe);

        //Actual
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper gracefully deals with null being
     * passed to the service.
     */
    @Test
    public void checkMapToModelWithNull() {

        //Expectation
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapDomainToModel(null);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper can map a Recipe with an empty ingredient set.
     */
    @Test
    public void checkMapToModelWithEmptyIngredientList() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty ingredient set.
        final Recipe recipeWithEmptyIngredientsSet = new Recipe.RecipeBuilder(recipe.getName(), recipe.getUploader(), recipe.getRecipeSummary())
                .withTags(recipe.getTags())
                .withIngredients(new HashMap<>())
                .withMethod(recipe.getMethod())
                .build();

        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipeWithEmptyIngredientsSet.getKey();

        //Expectation
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipeWithEmptyIngredientsSet.getName());
        expectedRecipeModel.setUploader(recipe.getUploader());
        expectedRecipeModel.setRecipeSummary(recipe.getRecipeSummary());
        expectedRecipeModel.setThumbsUpCount(recipe.getThumbsUpCount());
        expectedRecipeModel.setThumbsDownCount(recipe.getThumbsDownCount());
        expectedRecipeModel.setTags(recipe.getTags());
        expectedRecipeModel.setIngredients(recipeWithEmptyIngredientsSet.getIngredients());
        expectedRecipeModel.setMethod(recipeWithEmptyIngredientsSet.getMethod());

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapDomainToModel(recipeWithEmptyIngredientsSet);

        //Actual
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper can map a Recipe with an empty method.
     */
    @Test
    public void checkMapToModelWithEmptyMethod() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty method.
        final Recipe recipeWithEmptyMap = new Recipe.RecipeBuilder(recipe.getName(), recipe.getUploader(), recipe.getRecipeSummary())
                .withIngredients(recipe.getIngredients())
                .withMethod(new TreeMap<>())
                .build();

        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipeWithEmptyMap.getKey();

        //Expectation
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipeWithEmptyMap.getName());
        expectedRecipeModel.setIngredients(recipeWithEmptyMap.getIngredients());
        expectedRecipeModel.setMethod(recipeWithEmptyMap.getMethod());

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapDomainToModel(recipeWithEmptyMap);

        //Actual
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe with an empty recipeName.
     */
    @Test
    public void checkCantMapModelWithEmptyName() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty name.
        final Recipe recipeWithEmptyName = new Recipe.RecipeBuilder("          ", recipe.getUploader(), recipe.getRecipeSummary())
                .withIngredients(recipe.getIngredients())
                .withMethod(recipe.getMethod())
                .build();

        //Expectation - cannot map a recipe domain object with an empty recipeName
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapDomainToModel(recipeWithEmptyName);

        //Actual
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }


    /**
     * Test that the mappers can handle a situation when non-compulsory fields are not initialised.
     */
    @Test
    public void checkMapToModelNoMethodOrIngredients() {

        //Setup
        // generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have only required fields.
        final Recipe requiredFieldsOnlyRecipe = new Recipe.RecipeBuilder(recipe.getName(), recipe.getUploader(), recipe.getRecipeSummary())
                .build();

        //Expected
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(requiredFieldsOnlyRecipe.getKey());
        expectedRecipeModel.setName(requiredFieldsOnlyRecipe.getName());
        expectedRecipeModel.setIngredients(requiredFieldsOnlyRecipe.getIngredients());
        expectedRecipeModel.setMethod(requiredFieldsOnlyRecipe.getMethod());

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapDomainToModel(requiredFieldsOnlyRecipe);

        //Assertion
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);
    }

}
