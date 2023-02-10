package org.eatsy.appservice.testdatageneration;

import com.github.javafaker.Faker;
import org.eatsy.appservice.domain.Recipe;

import java.util.*;

/**
 * Interface for recipe test data creation utility methods.
 */
public interface RecipeDataFactory {

    /**
     * Uses the Java Faker library to create a Recipe object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe
     * @return a randomly generated Recipe object.
     */
    static Recipe generateRandomRecipe(final int maxIngredientSetSize, final int maxMethodMapSize) {

        //Setup
        //Faker object to generate the test data
        final Faker faker = new Faker();
        //Generate recipe name
        final String recipeName = faker.food().dish();
        //Generate uploader name
        final String uploader = faker.name().username();
        //Generate recipe summary
        final String recipeSummary = faker.shakespeare().romeoAndJulietQuote();
        //Generate thumbs up count
        //Generate tags
        final Set<String> tags = generateTagsSet(4);
        //Generate a set of ingredients for the recipe.
        final Map<Integer, String> generatedIngredientMap = generateIngredientMap(maxIngredientSetSize);
        //Generate a map of method steps.
        final Map<Integer, String> generatedMethodMap = generateMethodMap(maxMethodMapSize);

        //Construct random recipe
        final Recipe recipe = new Recipe.RecipeBuilder(recipeName, uploader, recipeSummary)
                .withTags(tags)
                .withIngredients(generatedIngredientMap)
                .withMethod(generatedMethodMap)
                .build();

        return recipe;

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
     * Generates a set of random ingredient steps.
     *
     * @param maxIngredientMapSize Max value for the generated number of ingredient steps in the recipe
     * @return a set of ingredient steps.
     */
    static Map<Integer, String> generateIngredientMap(final int maxIngredientMapSize) {

        //Create the ingredient map and define the number of steps in the method.
        final Map<Integer, String> ingredientMap = new TreeMap<>();
        final int numberOfIngredientSteps = generateNumber(maxIngredientMapSize);

        //Faker object to generate the test data
        final Faker faker = new Faker();

        //Populate the method map with random ingredient steps.
        for (int i = 0; i < numberOfIngredientSteps; i++) {
            ingredientMap.put(i, faker.food().ingredient());
        }

        return ingredientMap;
    }

    /**
     * Generates a set of random tags and size.
     *
     * @param maxTagsSetSize Max value for the generated number of tags in the recipe
     * @return a set of tags.
     */
    static Set<String> generateTagsSet(final int maxTagsSetSize) {

        //Create the tag set and define the number of tags in the recipe.
        final Set<String> tagsSet = new HashSet<>();
        final int numberOftags = generateNumber(maxTagsSetSize);

        //Populate the tags set with random tags.
        //Faker doesn't have food categories so using capital cities as dummy data
        for (int i = 0; i < numberOftags; i++) {
            tagsSet.add(new Faker().nation().capitalCity());
        }

        return tagsSet;

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
