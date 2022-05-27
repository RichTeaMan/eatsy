package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.RecipeDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParamters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.TreeMap;

/**
 * Recipe Mapper unit tests
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RecipeMapperTest {

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
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipe.getKey();

        //Expectation
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipe.getName());
        expectedRecipeModel.setIngredientSet(recipe.getIngredientSet());
        expectedRecipeModel.setMethod(recipe.getMethod());

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapToModel(recipe);

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
        final RecipeModel actualRecipeModel = recipeMapper.mapToModel(null);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper can map a Recipe with an empty ingredient set.
     */
    @Test
    public void checkMapToModelWithEmptyIngredientList() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty ingredient set.
        final Recipe recipeWithEmptyIngredientsSet = new Recipe.RecipeBuilder(recipe.getName())
                .withIngredientSet(new HashSet<>())
                .withMethod(recipe.getMethod())
                .build();

        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipeWithEmptyIngredientsSet.getKey();

        //Expectation
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipeWithEmptyIngredientsSet.getName());
        expectedRecipeModel.setIngredientSet(recipeWithEmptyIngredientsSet.getIngredientSet());
        expectedRecipeModel.setMethod(recipeWithEmptyIngredientsSet.getMethod());

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapToModel(recipeWithEmptyIngredientsSet);

        //Actual
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper can map a Recipe with an empty method.
     */
    @Test
    public void checkMapToModelWithEmptyMethod() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty method.
        final Recipe recipeWithEmptyMap = new Recipe.RecipeBuilder(recipe.getName())
                .withIngredientSet(recipe.getIngredientSet())
                .withMethod(new TreeMap<>())
                .build();

        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipeWithEmptyMap.getKey();

        //Expectation
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipeWithEmptyMap.getName());
        expectedRecipeModel.setIngredientSet(recipeWithEmptyMap.getIngredientSet());
        expectedRecipeModel.setMethod(recipeWithEmptyMap.getMethod());

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapToModel(recipeWithEmptyMap);

        //Actual
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe with an empty recipeName.
     */
    @Test
    public void checkCantMapModelWithEmptyName() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty name.
        final Recipe recipeWithEmptyName = new Recipe.RecipeBuilder("          ")
                .withIngredientSet(recipe.getIngredientSet())
                .withMethod(recipe.getMethod())
                .build();

        //Expectation - cannot map a recipe domain object with an empty recipeName
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapToModel(recipeWithEmptyName);

        //Actual
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

}
