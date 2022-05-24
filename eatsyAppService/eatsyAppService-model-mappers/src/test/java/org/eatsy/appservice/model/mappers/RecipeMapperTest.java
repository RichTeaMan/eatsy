package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.testdatageneration.RecipeDataFactory;
import org.eatsy.appservice.testdatageneration.RecipeDataFactoryHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    //Factory where the data generation methods are stored
    private RecipeDataFactory recipeDataFactory;

    //Max value for the generated number of ingredients in the recipe
    private int maxIngredientSetSize;

    //Max value for the generated number of method steps in the recipe
    private int maxMethodMapSize;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
        recipeDataFactory = new RecipeDataFactoryHandler();
        maxIngredientSetSize = 20;
        maxMethodMapSize = 10;
    }

    /**
     * This test checks the Recipe object is correctly mapped to a Recipe Model object.
     */
    @Test
    public void checkMapToModel() {

        //Setup - generate a recipe domain object to be mapped into a recipe model object.
        Recipe recipe = recipeDataFactory.generateRandomRecipe(maxIngredientSetSize, maxMethodMapSize);
        //Get this so that the assertion does not fail for different IDs being generated.
        String uniqueID = recipe.getKey();

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

        //Setup
        String recipeName = "Terry's chocolate orange";
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "Eat the chocolate orange in one go.");
        Set<String> ingredientSet = new HashSet<>();
        Recipe recipe = new Recipe(recipeName, ingredientSet, method);
        //Get this so that the assertion does not fail for different IDs being generated.
        String uniqueID = recipe.getKey();

        //Expectation
        RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipeName);
        expectedRecipeModel.setIngredientSet(ingredientSet);
        expectedRecipeModel.setMethod(method);

        //Test
        RecipeModel actualRecipeModel = recipeMapper.mapToModel(recipe);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper can map a Recipe with an empty method.
     */
    @Test
    public void checkMapToModelWithEmptyMethod() {

        //Setup
        String recipeName = "Fish finger sandwich";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Fish fingers");
        ingredientSet.add("Bread");

        Map<Integer, String> method = new HashMap<>();
        Recipe recipe = new Recipe(recipeName, ingredientSet, method);
        //Get this so that the assertion does not fail for different IDs being generated.
        String uniqueID = recipe.getKey();

        //Expectation
        RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setKey(uniqueID); //So assertion doesn't fail on an ID difference.
        expectedRecipeModel.setName(recipeName);
        expectedRecipeModel.setIngredientSet(ingredientSet);
        expectedRecipeModel.setMethod(method);

        //Test
        RecipeModel actualRecipeModel = recipeMapper.mapToModel(recipe);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Mapper cannot map a recipe with an empty recipeName.
     */
    @Test
    public void checkCantMapModelWithEmptyName() {

        //Setup
        String recipeName = "   ";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Eggs");
        ingredientSet.add("Chips");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "Cook chips");
        method.put(2, "Fry eggs");
        method.put(3, "cut up fried eggs over chips");
        Recipe recipe = new Recipe(recipeName, ingredientSet, method);

        //Expectation - cannot map a recipe domain object with an empty recipeName
        RecipeModel expectedRecipeModel = null;

        //Test
        RecipeModel actualRecipeModel = recipeMapper.mapToModel(recipe);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

}
