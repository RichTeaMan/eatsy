package org.eatsy.appservice.testdatageneration;

import com.github.javafaker.Faker;
import org.eatsy.appservice.persistence.model.RecipeEntity;

import java.util.*;

/**
 * Interface for recipe entity test data creation utility methods
 */
public interface RecipeEntityDataFactory {

    /**
     * Uses the Java Faker library to create a RecipeEntity object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe entity
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe entity
     * @return a randomly generated RecipeEntity object.
     */
    static RecipeEntity generateRandomRecipeEntity(final int maxIngredientSetSize, final int maxMethodMapSize) {

        //Setup
        final RecipeEntity recipeEntity = new RecipeEntity();
        //Faker object to generate the test data
        final Faker faker = new Faker();
        //Generate recipe name
        final String recipeName = faker.food().dish();
        //Generate a set of ingredients for the recipe.
        final Set<String> generatedIngredientSet = generateIngredientSet(maxIngredientSetSize);
        //Generate a map of method steps.
        final Map<Integer, String> generatedMethodMap = generateMethodMap(maxMethodMapSize);

        recipeEntity.setName(recipeName);
        recipeEntity.setIngredientSet(generatedIngredientSet);
        recipeEntity.setMethodMap(generatedMethodMap);
        return recipeEntity;

    }

    /**
     * Uses the Java Faker library to create a list of RecipeEntity objects
     *
     * @param maxNumberOfRecipes   Max value for the generated number of recipeEntities in the list.
     * @param maxIngredientSetSize Max value for the generated number of ingredients in each recipe entity
     * @param maxMethodMapSize     Max value for the generated number of method steps in each recipe entity
     * @return a randomly generated list of recipeEntity objects.
     */
    static List<RecipeEntity> generateRecipeEntityList(final int maxNumberOfRecipes, final int maxIngredientSetSize, final int maxMethodMapSize) {

        //Create the recipe list and define the number of recipes in the list.
        final List<RecipeEntity> recipeEntityList = new ArrayList<>();
        final int numberOfRecipesInList = generateNumber(maxNumberOfRecipes);

        //Populate the recipe list with random recipeModels.
        for (int i = 0; i < numberOfRecipesInList; i++) {
            recipeEntityList.add(generateRandomRecipeEntity(maxIngredientSetSize, maxMethodMapSize));
        }

        return recipeEntityList;
    }

    /**
     * Generates a set of random method steps.
     *
     * @param maxMethodMapSize Max value for the generated number of method steps in the recipe
     * @return a set of method steps.
     */
    static Map<Integer, String> generateMethodMap(final int maxMethodMapSize) {

        //Create the method map and define the number of steps in the method.
        final Map<Integer, String> methodMap = new TreeMap<>();
        final int numberOfMethodSteps = generateNumber(maxMethodMapSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

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
     * @return a set of ingredients.
     */
    static Set<String> generateIngredientSet(final int maxIngredientSetSize) {

        //Create the ingredient set and define the number of ingredients in the recipe.
        final Set<String> ingredientSet = new HashSet<>();
        final int numberOfIngredients = generateNumber(maxIngredientSetSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

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
    static int generateNumber(final int maxValue) {

        final int minValue = 1;
        final Random random = new Random();
        //Ensure min value is 1 (in the instance maxValue is generated as 0)
        final int generatedNumber = random.nextInt(maxValue - minValue + 1) + minValue;

        return generatedNumber;
    }

}
