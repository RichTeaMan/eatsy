package org.eatsy.appservice.service;

import com.github.javafaker.Faker;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.*;

/**
 * Recipe Factory unit tests for the Create Recipe Method
 */
//Define lifecycle of tests to be per class rather than per method default. Allows use of @BeforeAll
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CreateRecipeTests {

    /**
     * Class under test.
     */
    private RecipeFactory recipeFactory;

    //RecipeFactory dependent on RecipeMapper
    private RecipeMapper recipeMapper;

    //Max value for the generated number of ingredients in the recipe
    private int maxIngredientSetSize;

    //Max value for the generated number of method steps in the recipe
    private int maxMethodMapSize;

    @BeforeEach
    public void setup() {
        recipeMapper = new RecipeMapperHandler();
        recipeFactory = new RecipeFactoryHandler(recipeMapper);
        maxIngredientSetSize = 20;
        maxMethodMapSize = 10;
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
        //Create an input recipe model - this will also be the expected output from the method under test.
        RecipeModel inputRecipeModel = generateRandomRecipeModel();

        //Test
        RecipeModel actualRecipeModel = recipeFactory.createRecipe(inputRecipeModel);
        //Take the randomly generated key out of the assertion
        //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
        inputRecipeModel.setKey(actualRecipeModel.getKey());

        //Assert - check the returned model matches the request model used to create the recipe domain object.
        Assertions.assertEquals(inputRecipeModel, actualRecipeModel);
    }

    /**
     * Uses the Java Faker library to create a RecipeModel object
     *
     * @return a randomly generated RecipeModel object.
     */
    private RecipeModel generateRandomRecipeModel() {

        //Setup
        Faker faker = new Faker();
        RecipeModel recipeModel = new RecipeModel();
        //Generate recipe name
        String recipeName = faker.food().dish();
        //Generate a set of ingredients for the recipe.
        Set<String> generatedIngredientSet = generateIngredientSet(faker);
        //Generate a map of method steps.
        Map<Integer, String> generatedMethodMap = generateMethodMap(faker);

        recipeModel.setName(recipeName);
        recipeModel.setIngredientSet(generatedIngredientSet);
        recipeModel.setMethod(generatedMethodMap);
        return recipeModel;
    }

    /**
     * Generates a set of random method steps.
     *
     * @param faker The Faker object to generate random data for the test cases.
     * @return a set of method steps.
     */
    private Map<Integer, String> generateMethodMap(Faker faker) {

        //Create the method map and define the number of steps in the method.
        Map<Integer, String> methodMap = new TreeMap<>();
        int numberOfMethodSteps = generateNumber(maxMethodMapSize);

        //Populate the method map with random method steps.
        for (int i = 0; i < numberOfMethodSteps; i++) {
            //faker does not produce random recipe steps. However, Harry Potter quotes
            //are similar (in terms of short text sentence). So this will serve as representative test data.
            methodMap.put(i, faker.harryPotter().quote());
        }

        return methodMap;
    }

    /**
     * Generates a set of random ingredients and size.
     *
     * @param faker The Faker object to generate random data for the test cases.
     * @return a set of ingredients.
     */
    private Set<String> generateIngredientSet(Faker faker) {

        //Create the ingredient set and define the number of ingredients in the recipe.
        Set<String> ingredientSet = new HashSet<>();
        int numberOfIngredients = generateNumber(maxIngredientSetSize);

        //Populate the ingredient set with random ingredients.
        for (int i = 0; i < numberOfIngredients; i++) {
            ingredientSet.add(faker.food().ingredient());
        }

        return ingredientSet;

    }

    /**
     * Generates a random int between 1 and the maxValue parameter.
     *
     * @param maxValue the maximum number you want for the recipe field
     * @return the number of items that this recipe field will have.
     */
    private int generateNumber(int maxValue) {

        int minValue = 1;
        Random random = new Random();
        //Ensure min value is 1 (in the instance maxValue is generated as 0)
        int generatedNumber = random.nextInt(maxValue - minValue + 1) + minValue;

        return generatedNumber;
    }

}
