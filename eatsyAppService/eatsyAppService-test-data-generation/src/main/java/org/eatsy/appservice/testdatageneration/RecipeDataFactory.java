package org.eatsy.appservice.testdatageneration;

import org.eatsy.appservice.domain.Recipe;

import java.util.List;

/**
 * Interface for recipe test data creation
 */
public interface RecipeDataFactory {

    /**
     * Uses the Java Faker library to create a Recipe object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe
     * @return a randomly generated Recipe object.
     */
    Recipe generateRandomRecipe(int maxIngredientSetSize, int maxMethodMapSize);

    /**
     * Uses the Java Faker library to create a list of Recipe objects
     *
     * @param maxNumberOfRecipes   Max value for the generated number of recipe in the list.
     * @param maxIngredientSetSize Max value for the generated number of ingredients in each recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in each recipe
     * @return a randomly generated list of recipe objects.
     */
    List<Recipe> generateRecipeList(int maxNumberOfRecipes, int maxIngredientSetSize, int maxMethodMapSize);

}
