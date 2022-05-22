package org.eatsy.appservice.service;

import com.github.javafaker.Faker;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.model.mappers.RecipeMapperHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
        //Create an input recipe model
        RecipeModel inputRecipeModel = generateRandomRecipeModel();

    }

    /**
     * Uses the Java Faker library to create a RecipeModel object
     *
     * @return a randomly generated RecipeModel object.
     */
    private RecipeModel generateRandomRecipeModel() {

        Faker faker = new Faker();
        RecipeModel recipeModel = new RecipeModel();

        //Generate recipe name
        String recipeName = faker.food().dish();

        //Generate a set of ingredients for the recipe.
        Set<String> generatedIngredientSet = generateIngredientSet(faker);

        return recipeModel;
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
        int numberOfIngredients = generateNumberOfIngredients(20);

        //Populate the ingredient set with random ingredients.
        for (int i = 0; i < numberOfIngredients; i++ ){
            ingredientSet.add(faker.food().ingredient());
        }

        return ingredientSet;

    }

    /**
     * Generates a random int between 1 and the maxValue parameter.
     *
     * @param maxValue the maximum number of ingredients you want for the recipe
     * @return the number of ingredients that this recipe will have.
     */
    private int generateNumberOfIngredients(int maxValue) {

        int minValue = 1;
        Random random = new Random();
        //Ensure min value is 1 (in the instance maxValue is generated as 0)
        int numberOfIngredients = random.nextInt(maxValue - minValue + 1) + minValue;

        return numberOfIngredients;
    }

}
