package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParamters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.TreeMap;

/**
 * Recipe Factory unit tests for the Create Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CreateRecipeTests {

    /**
     * Class under test.
     */
    private RecipeFactory recipeFactory;


    @BeforeEach
    public void setup() {
        //RecipeFactory dependent on RecipeMapper
        final RecipeMapper recipeMapper = new RecipeMapperHandler();
        recipeFactory = new RecipeFactoryHandler(recipeMapper);

    }

    /**
     * The RecipeModel being returned from the recipeFactory is formed from
     * the newly created recipe domain object.
     * <p>
     * This test checks the RecipeModel response has the same content as the
     * recipeModel initially requested to the Eatsy App service.
     */
    @Test
    public void checkCreateRecipe() {

        //Setup
        //Create an input recipe model - this will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);

        //Test
        final RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert - check the returned model matches the request model used to create the recipe domain object.
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);
    }

    /**
     * Check the Recipe Factory gracefully deals with null being
     * passed to the service.
     */
    @Test
    public void checkCreateRecipeWithNull() {

        //Expectation
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeFactory.createRecipe(null);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory can create a recipe with an empty Ingredient List
     */
    @Test
    public void checkCreateRecipeWithEmptyIngredientList() {

        //Setup
        //Create an input recipe model(with an empty ingredients list)
        //This will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setIngredientSet(new HashSet<>());

        //Test
        final RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory can create a recipe with an empty method
     */
    @Test
    public void checkCreateRecipeWithEmptyMethod() {

        //Setup
        //Create an input recipe model(with an empty method list)
        //This will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setMethod(new TreeMap<>());

        //Test
        final RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory cannot create a recipe with an empty RecipeName field.
     */
    @Test
    public void checkCreateRecipeWithEmptyName() {

        //Setup
        //Create an input recipe model(with an empty method list)
        //This will also be the expected output from the method under test.
        final RecipeModel inputRecipeModel = RecipeModelDataFactory
                .generateRandomRecipeModel(EatsyRecipeTestParamters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParamters.MAX_METHOD_MAP_SIZE);
        inputRecipeModel.setName("         ");

        //Expectation - cannot create a recipe domain object without providing a recipe name.
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

}
