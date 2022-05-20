package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

/**
 * Recipe Factory unit tests
 */
//Define lifecycle of tests to be per class rather than per method default. Allows use of @BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RecipeFactoryTest {

    /**
     * Class under test.
     */
    private RecipeFactory recipeFactory;

    //RecipeFactory dependent on RecipeMapper
    private RecipeMapper recipeMapper;

    @BeforeAll
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
        recipeFactory = new RecipeFactoryHandler(recipeMapper);
    }

    /**
     * The RecipeModel being returned from the recipeFactory is formed from
     * the newly created recipe domain object.
     * <p>
     * This test checks the RecipeModel response has the same content as the
     * recipeModel initially requested to the Easty App service.
     */
    @Test
    public void checkCreateRecipe() {

        //Setup
        String recipeName = "Jam Toast";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Bread");
        ingredientSet.add("Jam");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "Toast Bread");
        method.put(2, "Apply Jam on the toast");

        //Expected
        RecipeModel inputRecipeModel = new RecipeModel();
        inputRecipeModel.setName(recipeName);
        inputRecipeModel.setIngredientSet(ingredientSet);
        inputRecipeModel.setMethod(method);
        //Get the UUID and apply to the actual recipeModel so the test doesn't fail on UUID generation
        String uniqueID = inputRecipeModel.getKey();

        //Test
        RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);
        actualRecipeModel.setKey(uniqueID); //So the unique IDs don't cause a fail
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
        String recipeName = "Peanut Jelly Sandwich";
        Set<String> ingredientSet = new HashSet<>();
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "Apply Peanut Butter to a slice of bread");
        method.put(2, "Apply Jam to a slice of bread");
        method.put(3, "Combine to create a sandwich");

        //Expectation
        RecipeModel inputRecipeModel = new RecipeModel();
        inputRecipeModel.setName(recipeName);
        inputRecipeModel.setIngredientSet(ingredientSet);
        inputRecipeModel.setMethod(method);
        //Get the UUID and apply to the actual recipeModel so the test doesn't fail on UUID generation
        String uniqueID = inputRecipeModel.getKey();

        //Test
        RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);
        actualRecipeModel.setKey(uniqueID); //So the unique IDs don't cause a fail
        //Assert - check the returned model matches the request model used to create the recipe domain object.

        //Assert
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);
    }

    /**
     * Check the Recipe Factory can create a recipe with an empty method
     */
    @Test
    public void checkCreateRecipeWithEmptyMethod() {

        //Setup
        String recipeName = "Tuna Mayo Sandwich";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Tuna");
        ingredientSet.add("Mayo");
        ingredientSet.add("Bread");
        Map<Integer, String> method = new HashMap<>();

        //Expectation
        RecipeModel inputRecipeModel = new RecipeModel();
        inputRecipeModel.setName(recipeName);
        inputRecipeModel.setIngredientSet(ingredientSet);
        inputRecipeModel.setMethod(method);
        //Get the UUID and apply to the actual recipeModel so the test doesn't fail on UUID generation
        String uniqueID = inputRecipeModel.getKey();

        //Test
        RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);
        actualRecipeModel.setKey(uniqueID); //So the unique IDs don't cause a fail
        //Assert - check the returned model matches the request model used to create the recipe domain object.

        //Assert
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);

    }

    /**
     * Check the Recipe Factory cannot create a recipe with an empty RecipeName field.
     */
    @Test
    public void checkCreateRecipeWithEmptyName() {

        //Setup
        String recipeName = "         ";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Ham");
        ingredientSet.add("Cheese");
        ingredientSet.add("Panini");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "combine Ham and cheese in the panini");
        method.put(2, "heat panini to melt cheese");

        RecipeModel inputRecipeModel = new RecipeModel();
        inputRecipeModel.setName(recipeName);
        inputRecipeModel.setIngredientSet(ingredientSet);
        inputRecipeModel.setMethod(method);

        //Expectation - cannot create a recipe domain object without providing a recipe name.
        RecipeModel expectedRecipeModel = null;

        //Test
        RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);

        //Assert
        Assertions.assertEquals(expectedRecipeModel, actualRecipeModel);
    }

    /**
     * Check the recipe factory correctly returns all recipes
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup

        //Populate the recipeCache with two recipes
        List<RecipeModel> expectedRecipeModelList = createRecipeModels();
        recipeFactory.createRecipe(expectedRecipeModelList.get(0));
        recipeFactory.createRecipe(expectedRecipeModelList.get(1));

        //Test

        List<RecipeModel> actualRecipeModelsList = recipeFactory.retrieveAllRecipes();
        //To ensure test doesn't fail on the randomly generated UUIDs
        expectedRecipeModelList.get(0).setKey(actualRecipeModelsList.get(0).getKey());
        expectedRecipeModelList.get(1).setKey(actualRecipeModelsList.get(1).getKey());

        //Assert
        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }

    /**
     * Create two recipe model objects. Expected result for the checkRetrieveAllRecipesSuccess test.
     */
    private List<RecipeModel> createRecipeModels() {
        String recipeName = "Ham and cheese panini";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Ham");
        ingredientSet.add("Cheese");
        ingredientSet.add("Panini");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "combine Ham and cheese in the panini");
        method.put(2, "heat panini to melt cheese");

        final RecipeModel inputRecipeModelOne = new RecipeModel();
        inputRecipeModelOne.setName(recipeName);
        inputRecipeModelOne.setIngredientSet(ingredientSet);
        inputRecipeModelOne.setMethod(method);

        String recipeNameTwo = "Jam and peanut butter sandwich";
        Set<String> ingredientSetTwo = new HashSet<>();
        ingredientSetTwo.add("Jam");
        ingredientSetTwo.add("Peanut butter");
        ingredientSetTwo.add("Sandwich");
        Map<Integer, String> methodTwo = new HashMap<>();
        methodTwo.put(1, "combine Jam and peanut butter in the sandwich");
        methodTwo.put(2, "Cut the crusts off");

        final RecipeModel inputRecipeModelTwo = new RecipeModel();
        inputRecipeModelTwo.setName(recipeNameTwo);
        inputRecipeModelTwo.setIngredientSet(ingredientSetTwo);
        inputRecipeModelTwo.setMethod(methodTwo);

        List<RecipeModel> recipeModelList = new ArrayList<>();
        recipeModelList.add(inputRecipeModelOne);
        recipeModelList.add(inputRecipeModelTwo);

        return recipeModelList;

    }

}
