package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Recipe Mapper unit tests
 */
//Define lifecycle of tests to be per class rather than per method default. Allows use of @BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeMapperTest {

    /**
     * Class under test.
     */
    private RecipeMapper recipeMapper;

    @BeforeAll
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
    }

    /**
     * This test checks the Recipe object is correctly mapped to a Recipe Model object.
     */
    @Test
    public void checkMapToModel() {
        //Setup
        String recipeName = "Beans on Toast";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Beans");
        ingredientSet.add("Toast");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "Microwave Beans");
        method.put(2, "Toast Bread");
        method.put(3, "Put Beans on the toast");
        Recipe recipe = new Recipe(recipeName, ingredientSet, method);

        //Expectation
        final RecipeModel expectedRecipeModel = new RecipeModel();
        expectedRecipeModel.setName(recipeName);
        expectedRecipeModel.setIngredientSet(ingredientSet);
        expectedRecipeModel.setMethod(method);

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

        //Expectation
        RecipeModel expectedRecipeModel = new RecipeModel();
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
