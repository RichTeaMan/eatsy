package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.TreeMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Recipe Map to Domain Mapper unit tests
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MapModelToDomainMapperTests {

    /**
     * Class under test.
     */
    private RecipeMapper recipeMapper;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
    }

    /**
     * This test checks the Recipe model is correctly mapped to a Recipe Domain object.
     */
    @Test
    public void checkMapModelToDomain() {

        //Setup
        //Generate a recipe model object to be mapped into a recipe domain model object.
        final RecipeModel recipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Expectation
        final Recipe expectedRecipe =
                new Recipe.RecipeBuilder(recipeModel.getName(), recipeModel.getUploader(), recipeModel.getRecipeSummary())
                        .withTags(recipeModel.getTags())
                        .withIngredients(recipeModel.getIngredients())
                        .withMethod(recipeModel.getMethod())
                        .build();
        //Set this so that the assertion doesn't fail when comparing the unique key field.
        recipeModel.setKey(expectedRecipe.getKey());

        //Test
        final Recipe actualRecipe = recipeMapper.mapModelToDomain(recipeModel);

        //Assertion
        Assertions.assertEquals(expectedRecipe, actualRecipe);

    }

    /**
     * Check the recipeMapper successfully assigns a unique key to the domain object
     * when no existing key exists for the model to be mapped
     */
    @Test
    public void checkMapToDomainWithNoExistingKey() {

        //Setup
        //Generate a recipe model object with no unique key assigned to be mapped into a recipe domain model object.
        final RecipeModel recipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Confirm that the recipeModel has a null key
        Assertions.assertNull(recipeModel.getKey());

        //Test
        final Recipe actualRecipe = recipeMapper.mapModelToDomain(recipeModel);

        //Assertion - check the domain object that is returned from the mapper has a unique key assigned.
        Assertions.assertNotNull(actualRecipe.getKey(), "Test failed due to the actualRecipe key being null");


    }

    /**
     * Check the recipe mapper gracefully deals with null being passed to the service.
     */
    @Test
    public void checkMapToDomainWithNull() {

        //Expectation
        final Recipe expectedRecipe = null;

        //Test
        final Recipe actualRecipe = recipeMapper.mapModelToDomain(null);

        //Assert
        Assertions.assertEquals(expectedRecipe, actualRecipe);

    }

    /**
     * Check the Recipe Mapper can map a recipe with an empty ingredient set.
     */
    @Test
    public void checkMapToDomainWithEmptyIngredientsList() {

        //Setup
        //Generate a recipe model object to be mapped into a recipe domain object.
        final RecipeModel recipeModel = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe model have an empty ingredient set.
        final RecipeModel recipeModelWithEmptyIngredientSet = new RecipeModel();
        recipeModelWithEmptyIngredientSet.setName(recipeModel.getName());
        recipeModelWithEmptyIngredientSet.setUploader(recipeModel.getUploader());
        recipeModelWithEmptyIngredientSet.setRecipeSummary(recipeModel.getRecipeSummary());
        recipeModelWithEmptyIngredientSet.setThumbsUpCount(recipeModel.getThumbsUpCount());
        recipeModelWithEmptyIngredientSet.setThumbsDownCount(recipeModel.getThumbsDownCount());
        recipeModelWithEmptyIngredientSet.setTags(recipeModel.getTags());
        recipeModelWithEmptyIngredientSet.setIngredients(new HashMap<>());
        recipeModelWithEmptyIngredientSet.setMethod(recipeModel.getMethod());

        //Expectation
        final Recipe expectedDomainRecipe =
                new Recipe.RecipeBuilder(
                        recipeModelWithEmptyIngredientSet.getName(),
                        recipeModelWithEmptyIngredientSet.getUploader(),
                        recipeModelWithEmptyIngredientSet.getRecipeSummary())
                        .withTags(recipeModelWithEmptyIngredientSet.getTags())
                        .withIngredients(recipeModelWithEmptyIngredientSet.getIngredients())
                        .withMethod(recipeModelWithEmptyIngredientSet.getMethod())
                        .build();

        //Set this so that the assertion doesn't fail when comparing the unique key field.
        recipeModelWithEmptyIngredientSet.setKey(expectedDomainRecipe.getKey());

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapModelToDomain(recipeModelWithEmptyIngredientSet);

        //Assertion
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);
        
    }

    /**
     * Check the Recipe Mapper can map a RecipeModel with an empty method.
     */
    @Test
    public void checkMapToDomainWithEmptyMethod() {

        //Setup
        //Generate a recipe model object to be mapped into recipe domain object
        final RecipeModel recipeModel = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe model have an empty method.
        final RecipeModel recipeModelWithEmptyMap = new RecipeModel();
        recipeModelWithEmptyMap.setName(recipeModel.getName());
        recipeModelWithEmptyMap.setIngredients(recipeModel.getIngredients());
        recipeModelWithEmptyMap.setMethod(new TreeMap<>());

        //Exception
        final Recipe expectedDomainRecipe =
                new Recipe.RecipeBuilder(
                        recipeModelWithEmptyMap.getName(),
                        recipeModelWithEmptyMap.getUploader(),
                        recipeModelWithEmptyMap.getRecipeSummary())
                        .withIngredients(recipeModelWithEmptyMap.getIngredients())
                        .withMethod(recipeModelWithEmptyMap.getMethod())
                        .build();

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapModelToDomain(recipeModelWithEmptyMap);

        //Assertion (assertJ) - exclude the key field which will be different due to unique key generation.
        assertThat(expectedDomainRecipe)
                .usingRecursiveComparison().ignoringFields("key")
                .isEqualTo(actualDomainRecipe);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe model with an empty recipeName.
     */
    @Test
    public void checkCantMapToDomainWithEmptyName() {

        //Setup
        //Generate a recipe model object to be mapped into recipe domain object
        final RecipeModel recipeModel = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe model have an empty name
        final RecipeModel recipeModelWithEmptyRecipeName = new RecipeModel();
        recipeModelWithEmptyRecipeName.setName("         ");
        recipeModelWithEmptyRecipeName.setIngredients(recipeModel.getIngredients());
        recipeModelWithEmptyRecipeName.setMethod(recipeModel.getMethod());

        //Expectation - cannot map a recipe model with an empty recipeName
        final Recipe expectedDomainRecipe = null;

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapModelToDomain(recipeModelWithEmptyRecipeName);

        //Actual
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

    /**
     * Test that the mappers can handle a situation when non-compulsory fields are not initialised.
     */
    @Test
    public void checkMapToDomainNoMethodOrIngredients() {

        //Setup
        //Generate a recipe model object to be mapped into recipe domain object
        final RecipeModel recipeModel = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Make the recipe model have only the required fields
        final RecipeModel requiredFieldsOnlyRecipeModel = new RecipeModel();
        requiredFieldsOnlyRecipeModel.setName(recipeModel.getName());

        //Expected
        final Recipe expectedDomainRecipe = new Recipe
                .RecipeBuilder(requiredFieldsOnlyRecipeModel.getName(), recipeModel.getUploader(), recipeModel.getRecipeSummary())
                .build();

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapModelToDomain(requiredFieldsOnlyRecipeModel);

        //Assertion (assertJ) - exclude the key field which will be different due to unique key generation.
        assertThat(expectedDomainRecipe)
                .usingRecursiveComparison().ignoringFields("key")
                .isEqualTo(actualDomainRecipe);

    }

}
