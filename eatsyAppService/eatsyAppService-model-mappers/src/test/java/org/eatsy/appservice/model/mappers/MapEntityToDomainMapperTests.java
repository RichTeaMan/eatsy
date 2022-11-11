package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.testdatageneration.RecipeEntityDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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

}