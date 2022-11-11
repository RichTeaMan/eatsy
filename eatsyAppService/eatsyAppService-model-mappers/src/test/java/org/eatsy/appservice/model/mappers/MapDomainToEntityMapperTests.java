package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.testdatageneration.RecipeDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.TreeMap;

/**
 * Recipe Map From Domain to Entity Mapper unit tests
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MapDomainToEntityMapperTests {

    /**
     * Class under test.
     */
    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
    }

    /**
     * This test checks the Recipe object is correctly mapped to a Recipe Entity object.
     */
    @Test
    public void checkMapToEntity() {

        //Setup - generate a recipe domain object to be mapped into a recipe entity object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipe.getKey();

        //Expectation
        final RecipeEntity expectedRecipeEntity = new RecipeEntity();
        expectedRecipeEntity.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeEntity.setName(recipe.getName());
        expectedRecipeEntity.setIngredientSet(recipe.getIngredientSet());
        expectedRecipeEntity.setMethodMap(recipe.getMethod());

        //Test
        final RecipeEntity actualRecipeEntity = recipeMapper.mapDomainToEntity(recipe);

        //Actual
        Assertions.assertEquals(expectedRecipeEntity, actualRecipeEntity);

    }

    /**
     * Check the Recipe Mapper gracefully deals with null being
     * passed to the service.
     */
    @Test
    public void checkMapToEntityWithNull() {

        //Expectation
        final RecipeEntity expectedRecipeEntity = null;

        //Test
        final RecipeEntity actualRecipeEntity = recipeMapper.mapDomainToEntity(null);

        //Assert
        Assertions.assertEquals(expectedRecipeEntity, actualRecipeEntity);

    }

    /**
     * Check the Recipe Mapper can map a Recipe with an empty ingredient set.
     */
    @Test
    public void checkMapToEntityWithEmptyIngredientList() {

        //Setup - generate a recipe domain object to be mapped into a recipe entity object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty ingredient set.
        final Recipe recipeWithEmptyIngredientsSet = new Recipe.RecipeBuilder(recipe.getName())
                .withIngredientSet(new HashSet<>())
                .withMethod(recipe.getMethod())
                .build();

        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipeWithEmptyIngredientsSet.getKey();

        //Expectation
        final RecipeEntity expectedRecipeEntity = new RecipeEntity();
        expectedRecipeEntity.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeEntity.setName(recipeWithEmptyIngredientsSet.getName());
        expectedRecipeEntity.setIngredientSet(recipeWithEmptyIngredientsSet.getIngredientSet());
        expectedRecipeEntity.setMethodMap(recipeWithEmptyIngredientsSet.getMethod());

        //Test
        final RecipeEntity actualRecipeEntity = recipeMapper.mapDomainToEntity(recipeWithEmptyIngredientsSet);

        //Actual
        Assertions.assertEquals(expectedRecipeEntity, actualRecipeEntity);

    }

    /**
     * Check the Recipe Mapper can map a Recipe with an empty method.
     */
    @Test
    public void checkMapToEntityWithEmptyMethod() {

        //Setup - generate a recipe domain object to be mapped into a recipe entity object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty method.
        final Recipe recipeWithEmptyMap = new Recipe.RecipeBuilder(recipe.getName())
                .withIngredientSet(recipe.getIngredientSet())
                .withMethod(new TreeMap<>())
                .build();

        //Get this so that the assertion does not fail for different IDs being generated.
        final String uniqueID = recipeWithEmptyMap.getKey();

        //Expectation
        final RecipeEntity expectedRecipeEntity = new RecipeEntity();
        expectedRecipeEntity.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeEntity.setName(recipeWithEmptyMap.getName());
        expectedRecipeEntity.setIngredientSet(recipeWithEmptyMap.getIngredientSet());
        expectedRecipeEntity.setMethodMap(recipeWithEmptyMap.getMethod());

        //Test
        final RecipeEntity actualRecipeEntity = recipeMapper.mapDomainToEntity(recipeWithEmptyMap);

        //Actual
        Assertions.assertEquals(expectedRecipeEntity, actualRecipeEntity);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe with an empty recipeName.
     */
    @Test
    public void checkCantMapEntityWithEmptyName() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        final Recipe recipe = RecipeDataFactory.generateRandomRecipe(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe have an empty name.
        final Recipe recipeWithEmptyName = new Recipe.RecipeBuilder("          ")
                .withIngredientSet(recipe.getIngredientSet())
                .withMethod(recipe.getMethod())
                .build();

        //Expectation - cannot map a recipe domain object with an empty recipeName
        final RecipeEntity expectedRecipeEntity = null;

        //Test
        final RecipeEntity actualRecipeEntity = recipeMapper.mapDomainToEntity(recipeWithEmptyName);

        //Actual
        Assertions.assertEquals(expectedRecipeEntity, actualRecipeEntity);

    }

}
