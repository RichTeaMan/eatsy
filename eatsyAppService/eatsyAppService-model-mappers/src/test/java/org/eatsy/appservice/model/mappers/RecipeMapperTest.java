package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Recipe Mapper unit tests
 */
public class RecipeMapperTest {

    /**
     * Class under test.
     */
    private RecipeMapper recipeMapper;

    @Before
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
    }


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
        Assert.assertEquals(expectedRecipeModel, actualRecipeModel);

    }

    @Test
    public void checkMapToModelWithNull() {

        //Expectation
        final RecipeModel expectedRecipeModel = null;

        //Test
        final RecipeModel actualRecipeModel = recipeMapper.mapToModel(null);

        //Assert
        Assert.assertEquals(expectedRecipeModel, actualRecipeModel);

    }


}
