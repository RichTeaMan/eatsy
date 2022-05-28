package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Recipe Map to Domain Mapper unit tests
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class MapToDomainMapperTests {

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
    public void checkMapToDomain() {

        //Setup
        //Generate a recipe model object to be mapped into a recipe domain model object./
        final RecipeModel recipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Expectation
        final Recipe expectedRecipe = new Recipe.RecipeBuilder(recipeModel.getName())
                .withIngredientSet(recipeModel.getIngredientSet())
                .withMethod(recipeModel.getMethod())
                .build();
        //Set this so that the assertion doesn't fail when comparing the unique key field.
        recipeModel.setKey(expectedRecipe.getKey());

        //Test
        final Recipe actualRecipe = recipeMapper.mapToDomain(recipeModel);

        //Assertion (assertJ) - exclude the key field which will be different due to unique key generation.
        assertThat(expectedRecipe)
                .usingRecursiveComparison().ignoringFields("key")
                .isEqualTo(actualRecipe);

    }

}
