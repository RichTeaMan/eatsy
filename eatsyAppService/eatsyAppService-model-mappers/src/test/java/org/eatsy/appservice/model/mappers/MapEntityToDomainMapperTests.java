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
import java.util.Map;
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
        final Recipe expectedRecipe = createExpectedRecipe(recipeEntity, recipeEntity.getUploader(), recipeEntity.getRecipeSummary(), recipeEntity.getThumbsUpCount(), recipeEntity.getThumbsDownCount());
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
        final RecipeEntity recipeEntity;
        //Generate a recipe entity object to be mapped into a recipe domain object.
        recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Make the recipe entity have an empty ingredient set.
        final RecipeEntity recipeEntityWithEmptyIngredientSet = createRecipeEntity(
                recipeEntity, new HashMap<String, String>(), recipeEntity.getMethodMap(),
                recipeEntity.getName(), recipeEntity.getUploader(), recipeEntity.getRecipeSummary());

        //Expectation
        final Recipe expectedDomainRecipe = createExpectedRecipe(recipeEntityWithEmptyIngredientSet, recipeEntity.getUploader(), recipeEntity.getRecipeSummary(), recipeEntity.getThumbsUpCount(), recipeEntity.getThumbsDownCount());
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
        final RecipeEntity recipeEntityWithEmptyMap = createRecipeEntity(
                recipeEntity, recipeEntity.getIngredientsMap(), new TreeMap<>(),
                recipeEntity.getName(), recipeEntity.getUploader(), recipeEntity.getRecipeSummary());

        //Exception
        final Recipe expectedDomainRecipe = createExpectedRecipe(recipeEntityWithEmptyMap, recipeEntity.getUploader(), recipeEntity.getRecipeSummary(), recipeEntity.getThumbsUpCount(), recipeEntity.getThumbsDownCount());
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
        final RecipeEntity recipeEntityWithEmptyRecipeName = createRecipeEntity(
                recipeEntity, recipeEntity.getIngredientsMap(), recipeEntity.getMethodMap(),
                "         ", recipeEntity.getUploader(), recipeEntity.getRecipeSummary());

        //Expectation - cannot map a recipe model with an empty recipeName
        final Recipe expectedDomainRecipe = null;

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(recipeEntityWithEmptyRecipeName);

        //Actual
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe entity with an empty recipeUploader.
     */
    @Test
    public void checkCantMapToDomainWithEmptyUploader() {

        //Setup
        //Generate a recipe entity object to be mapped into recipe domain object
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Make the recipe entity have an empty uploader
        final RecipeEntity recipeEntityWithEmptyRecipeUploader = createRecipeEntity(
                recipeEntity, recipeEntity.getIngredientsMap(), recipeEntity.getMethodMap(),
                recipeEntity.getName(), "    ", recipeEntity.getRecipeSummary());

        //Expectation - cannot map a recipe model with an empty recipeUploader
        final Recipe expectedDomainRecipe = null;

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(recipeEntityWithEmptyRecipeUploader);

        //Actual
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe entity with an empty recipeSummary.
     */
    @Test
    public void checkCantMapToDomainWithEmptySummary() {

        //Setup
        //Generate a recipe entity object to be mapped into recipe domain object
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Make the recipe entity have an empty summary
        final RecipeEntity recipeEntityWithEmptyRecipeSummary = createRecipeEntity(
                recipeEntity, recipeEntity.getIngredientsMap(), recipeEntity.getMethodMap(),
                recipeEntity.getName(), recipeEntity.getUploader(), "         ");

        //Expectation - cannot map a recipe model with an empty recipeSummary
        final Recipe expectedDomainRecipe = null;

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(recipeEntityWithEmptyRecipeSummary);

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
        final Recipe expectedDomainRecipe = createRequiredFieldsOnlyDomainRecipe(requiredFieldsOnlyRecipeEntity);
        //Set this so that the assertion doesn't fail when comparing the unique key field.
        requiredFieldsOnlyRecipeEntity.setKey(expectedDomainRecipe.getKey());

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(requiredFieldsOnlyRecipeEntity);

        //Assertion
        Assertions.assertEquals(expectedDomainRecipe, actualDomainRecipe);

    }

    /**
     * Method to create the expected recipe object based on the recipeEntity object.
     * This will be used in the tests so that the actual recipe generated by the mapper under test can
     * be assessed against the expected result.
     * specific properties of the recipyEntity are provided in addition to the recipeEntity to allow the method to be used for different scenarios
     *
     * @param recipeEntity    object to be mapped to domain object
     * @param uploader        property of the recipeEntity
     * @param recipeSummary   property of the recipeEntity
     * @param thumbsUpCount   property of the recipeEntity
     * @param thumbsDownCount property of the recipeEntity
     * @return the expected Recipe object that has been created from the recipeEntity object
     */
    private Recipe createExpectedRecipe(final RecipeEntity recipeEntity, final String uploader, final String recipeSummary, final Integer thumbsUpCount, final Integer thumbsDownCount) {
        return new Recipe.RecipeBuilder(recipeEntity.getName(), uploader, recipeSummary)
                .withTags(recipeEntity.getTags())
                .withThumbsUpCount(thumbsUpCount)
                .withThumbsDownCount(thumbsDownCount)
                .withIngredients(recipeEntity.getIngredientsMap())
                .withMethod(recipeEntity.getMethodMap())
                .build();
    }

    /**
     * Method to create the required recipeEntity object (from a randomly generated one) to test a specific edge case
     * of the mapper under test
     * specific properties of the recipyEntity are provided in addition to the recipeEntity to allow the method to be used for different scenarios
     *
     * @param recipeEntity object to be mapped to domain object
     * @param ingredients  property of the recipeEntity
     * @param method       property of the recipeEntity
     * @param name         property of the recipeEntity
     * @param uploader     property of the recipeEntity
     * @param summary      property of the recipeEntity
     * @return the expected recipeEntityObject with the specified changes for the required test condition
     */
    private RecipeEntity createRecipeEntity(
            final RecipeEntity recipeEntity, final Map ingredients, final Map method,
            final String name, final String uploader, final String summary) {

        final RecipeEntity recipeEntityWithEmptyIngredientSet = new RecipeEntity();
        recipeEntityWithEmptyIngredientSet.setName(name);
        recipeEntityWithEmptyIngredientSet.setUploader(uploader);
        recipeEntityWithEmptyIngredientSet.setRecipeSummary(summary);
        recipeEntityWithEmptyIngredientSet.setThumbsUpCount(recipeEntity.getThumbsUpCount());
        recipeEntityWithEmptyIngredientSet.setThumbsDownCount(recipeEntity.getThumbsDownCount());
        recipeEntityWithEmptyIngredientSet.setTags(recipeEntity.getTags());
        recipeEntityWithEmptyIngredientSet.setIngredientsMap(ingredients);
        recipeEntityWithEmptyIngredientSet.setMethodMap(method);

        return recipeEntityWithEmptyIngredientSet;
    }

    /**
     * @param requiredFieldsOnlyRecipeEntity recipeEntityobject with only the required fields populated
     * @return equivalent domain object with value only for required fields.
     */
    private Recipe createRequiredFieldsOnlyDomainRecipe(final RecipeEntity requiredFieldsOnlyRecipeEntity) {
        final Recipe expectedDomainRecipe = new Recipe
                .RecipeBuilder(
                requiredFieldsOnlyRecipeEntity.getName(),
                requiredFieldsOnlyRecipeEntity.getUploader(),
                requiredFieldsOnlyRecipeEntity.getRecipeSummary())
                .withThumbsUpCount(null) //As they are null in the entity being mapped
                .withThumbsDownCount(null)//As they are null in the entity being mapped
                .withTags(new HashSet<>())
                .withIngredients(new HashMap<>())
                .withMethod(new HashMap<>())
                .build();
        return expectedDomainRecipe;
    }

}