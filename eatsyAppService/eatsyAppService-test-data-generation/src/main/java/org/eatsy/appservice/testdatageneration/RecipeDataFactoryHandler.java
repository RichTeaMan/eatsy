package org.eatsy.appservice.testdatageneration;

import com.github.javafaker.Faker;
import org.eatsy.appservice.domain.Recipe;

import java.util.*;

/**
 * Recipe Data Factory implementation
 */
public class RecipeDataFactoryHandler implements RecipeDataFactory {

    //Faker object to generate the test data
    private final Faker faker = new Faker();

    /**
     * Uses the Java Faker library to create a Recipe object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe
     * @return a randomly generated Recipe object.
     */
    @Override
    public Recipe generateRandomRecipe(final int maxIngredientSetSize, final int maxMethodMapSize) {

        //Setup
        //Generate recipe name
        String recipeName = faker.food().dish();
        //Generate a set of ingredients for the recipe.
        Set<String> generatedIngredientSet = generateIngredientSet(faker, maxIngredientSetSize);
        //Generate a map of method steps.
        Map<Integer, String> generatedMethodMap = generateMethodMap(faker, maxMethodMapSize);

        //Construct random recipe
        Recipe recipe = new Recipe(recipeName, generatedIngredientSet, generatedMethodMap);

        return recipe;

    }

    /**
     * Uses the Java Faker library to create a list of Recipe objects
     *
     * @param maxNumberOfRecipes   Max value for the generated number of recipe in the list.
     * @param maxIngredientSetSize Max value for the generated number of ingredients in each recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in each recipe
     * @return a randomly generated list of recipe objects.
     */
    @Override
    public List<Recipe> generateRecipeList(int maxNumberOfRecipes, int maxIngredientSetSize, int maxMethodMapSize) {

        //Create the recipe list and define the number of recipes in the list.
        List<Recipe> recipeList = new ArrayList<>();
        int numberOfRecipesInList = generateNumber(maxNumberOfRecipes);

        //Populate the recipe list with random recipeModels.
        for (int i = 0; i < numberOfRecipesInList; i++) {
            recipeList.add(generateRandomRecipe(maxIngredientSetSize, maxMethodMapSize));
        }

        return recipeList;
    }

    /**
     * Generates a set of random method steps.
     *
     * @param maxMethodMapSize Max value for the generated number of method steps in the recipe
     * @param faker            The Faker object to generate random data for the test cases.
     * @return a set of method steps.
     */
    private Map<Integer, String> generateMethodMap(Faker faker, final int maxMethodMapSize) {

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
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe
     * @param faker                The Faker object to generate random data for the test cases.
     * @return a set of ingredients.
     */
    private Set<String> generateIngredientSet(Faker faker, final int maxIngredientSetSize) {

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
