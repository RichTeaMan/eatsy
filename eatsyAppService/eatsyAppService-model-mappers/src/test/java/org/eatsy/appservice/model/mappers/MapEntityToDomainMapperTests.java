package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.testdatageneration.RecipeEntityDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        final Recipe expectedRecipe = new Recipe.RecipeBuilder(recipeEntity.getName())
                .withIngredientSet(recipeEntity.getIngredientSet())
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
        recipeEntityWithEmptyIngredientSet.setIngredientSet(new HashSet<>());
        recipeEntityWithEmptyIngredientSet.setMethodMap(recipeEntity.getMethodMap());

        //Expectation
        final Recipe expectedDomainRecipe = new Recipe.RecipeBuilder(recipeEntityWithEmptyIngredientSet.getName())
                .withIngredientSet(recipeEntityWithEmptyIngredientSet.getIngredientSet())
                .withMethod(recipeEntityWithEmptyIngredientSet.getMethodMap())
                .build();

        //Test
        final Recipe actualDomainRecipe = recipeMapper.mapEntityToDomain(recipeEntityWithEmptyIngredientSet);

        //Assertion (assertJ) - exclude the key field which will be different due to unique key generation.
        assertThat(expectedDomainRecipe)
                .usingRecursiveComparison().ignoringFields("key")
                .isEqualTo(actualDomainRecipe);

    }

}