package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.testdatageneration.RecipeEntityDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * Recipe Map for Entity to Recipe Domain Mapper unit tests
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MapEntityToDomainMapperTests {

    /**
     * Class under test.
     */
    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
    }

    /**
     * This test checks the Recipe Entity is correctly mapped to a Recipe Domain object.
     */
    @Test
    public void checkMapEntityToDomain() {

        //Setup
        //Generate a recipe entity object to be mapped into a recipe domain model object.
        final RecipeEntity recipeEntity = RecipeEntityDataFactory
                .generateRandomRecipeEntity(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Expectation
        final Recipe expectedRecipe = new Recipe.RecipeBuilder(recipeEntity.getName(), recipeEntity.getUploader(), recipeEntity.getRecipeSummary())
                .withTags(recipeEntity.getTags())
                .withIngredients(recipeEntity.getIngredientsMap())
                .withMethod(recipeEntity.getMethodMap())
                .build();
        //Set this so that the assertion doesn't fail when comparing the unique key field.
        recipeEntity.setKey(expectedRecipe.getKey());

        //Test
        final Recipe actualRecipe = recipeMapper.mapEntityToDomain(recipeEntity);

        //Assertion
        Assertions.assertEquals(expectedRecipe, actualRecipe);

    }

    /**
     * Check the recipe mapper gracefully deals with null being passed to the service.
     */
    @Test
    public void checkMapToDomainWithNull() {

        //Expectation
        final Recipe expectedRecipe = null;

        //Test
        final Recipe actualRecipe = recipeMapper.mapEntityToDomain(null);

        //Assert
        Assertions.assertEquals(expectedRecipe, actualRecipe);

    }

    /**
     * Check the Recipe Mapper can map a recipe with an empty ingredient set.
     */
    @Test
    public void checkMapToDomainWithEmptyIngredientsList() {

        //Setup
        //Generate a recipe entity object to be mapped into a recipe domain object.
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe entity have an empty ingredient set.
        final RecipeEntity recipeEntityWithEmptyIngredientSet = new RecipeEntity();
        recipeEntityWithEmptyIngredientSet.setName(recipeEntity.getName());
        recipeEntityWithEmptyIngredientSet.setUploader(recipeEntity.getUploader());
        recipeEntityWithEmptyIngredientSet.setRecipeSummary(recipeEntity.getRecipeSummary());
        recipeEntityWithEmptyIngredientSet.setThumbsUpCount(recipeEntity.getThumbsUpCount());
        recipeEntityWithEmptyIngredientSet.setThumbsDownCount(recipeEntity.getThumbsDownCount());
        recipeEntityWithEmptyIngredientSet.setTags(recipeEntity.getTags());
        recipeEntityWithEmptyIngredientSet.setIngredientsMap(new HashMap<>());
        recipeEntityWithEmptyIngredientSet.setMethodMap(recipeEntity.getMethodMap());

        //Expectation
        final Recipe expectedDomainRecipe =
                new Recipe.RecipeBuilder(recipeEntityWithEmptyIngredientSet.getName(), recipeEntity.getUploader(), recipeEntity.getRecipeSummary())
                        .withTags(recipeEntityWithEmptyIngredientSet.getTags())
                        .withIngredients(recipeEntityWithEmptyIngredientSet.getIngredientsMap())
                        .withMethod(recipeEntityWithEmptyIngredientSet.getMethodMap())
                        .build();
        //Set this so that the assertion doesn't fail when comparing the unique key field.
        recipeEntityWithEmptyIngredientSet.setKey(expectedDomainRecipe.getKey());

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(recipeEntityWithEmptyIngredientSet);

        //Assertion
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

    /**
     * Check the Recipe Mapper can map a RecipeEntity with an empty method.
     */
    @Test
    public void checkMapToDomainWithEmptyMethod() {

        //Setup
        //Generate a recipe entity object to be mapped into recipe domain object
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe entity have an empty method.
        final RecipeEntity recipeEntityWithEmptyMap = new RecipeEntity();
        recipeEntityWithEmptyMap.setName(recipeEntity.getName());
        recipeEntityWithEmptyMap.setUploader(recipeEntity.getUploader());
        recipeEntityWithEmptyMap.setRecipeSummary(recipeEntity.getRecipeSummary());
        recipeEntityWithEmptyMap.setThumbsUpCount(recipeEntity.getThumbsUpCount());
        recipeEntityWithEmptyMap.setThumbsDownCount(recipeEntity.getThumbsDownCount());
        recipeEntityWithEmptyMap.setTags(recipeEntity.getTags());
        recipeEntityWithEmptyMap.setIngredientsMap(recipeEntity.getIngredientsMap());
        recipeEntityWithEmptyMap.setMethodMap(new TreeMap<>());

        //Exception
        final Recipe expectedDomainRecipe =
                new Recipe.RecipeBuilder(recipeEntityWithEmptyMap.getName(), recipeEntity.getUploader(), recipeEntity.getRecipeSummary())
                        .withTags(recipeEntityWithEmptyMap.getTags())
                        .withIngredients(recipeEntityWithEmptyMap.getIngredientsMap())
                        .withMethod(recipeEntityWithEmptyMap.getMethodMap())
                        .build();
        //Set this so that the assertion doesn't fail when comparing the unique key field.
        recipeEntityWithEmptyMap.setKey(expectedDomainRecipe.getKey());

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(recipeEntityWithEmptyMap);

        //Assertion
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe entity with an empty recipeName.
     */
    @Test
    public void checkCantMapToDomainWithEmptyName() {

        //Setup
        //Generate a recipe entity object to be mapped into recipe domain object
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe entity have an empty name
        final RecipeEntity recipeEntityWithEmptyRecipeName = new RecipeEntity();
        recipeEntityWithEmptyRecipeName.setName("         ");
        recipeEntityWithEmptyRecipeName.setUploader(recipeEntity.getUploader());
        recipeEntityWithEmptyRecipeName.setRecipeSummary(recipeEntity.getRecipeSummary());
        recipeEntityWithEmptyRecipeName.setThumbsUpCount(recipeEntity.getThumbsUpCount());
        recipeEntityWithEmptyRecipeName.setThumbsDownCount(recipeEntity.getThumbsDownCount());
        recipeEntityWithEmptyRecipeName.setTags(recipeEntity.getTags());
        recipeEntityWithEmptyRecipeName.setIngredientsMap(recipeEntity.getIngredientsMap());
        recipeEntityWithEmptyRecipeName.setMethodMap(recipeEntity.getMethodMap());

        //Expectation - cannot map a recipe model with an empty recipeName
        final Recipe expectedDomainRecipe = null;

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(recipeEntityWithEmptyRecipeName);

        //Actual
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

    /**
     * Test that the mappers can handle a situation when non-compulsory fields are not populated.
     */
    @Test
    public void checkMapToDomainNoOptionalFields() {

        //Setup
        //Generate a recipe entity object to be mapped into recipe domain object
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe model have only the required fields
        final RecipeEntity requiredFieldsOnlyRecipeEntity = new RecipeEntity();
        requiredFieldsOnlyRecipeEntity.setName(recipeEntity.getName());
        requiredFieldsOnlyRecipeEntity.setUploader(recipeEntity.getUploader());
        requiredFieldsOnlyRecipeEntity.setRecipeSummary(recipeEntity.getRecipeSummary());

        //Expected
        final Recipe expectedDomainRecipe = new Recipe
                .RecipeBuilder(
                requiredFieldsOnlyRecipeEntity.getName(),
                requiredFieldsOnlyRecipeEntity.getUploader(),
                requiredFieldsOnlyRecipeEntity.getRecipeSummary())
                .withTags(new HashSet<>())
                .withIngredients(new HashMap<>())
                .withMethod(new HashMap<>())
                .build();
        //Set this so that the assertion doesn't fail when comparing the unique key field.
        requiredFieldsOnlyRecipeEntity.setKey(expectedDomainRecipe.getKey());

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(requiredFieldsOnlyRecipeEntity);

        //Assertion
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

}