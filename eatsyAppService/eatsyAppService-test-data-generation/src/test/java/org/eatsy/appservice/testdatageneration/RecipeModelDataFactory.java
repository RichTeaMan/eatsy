package org.eatsy.appservice.testdatageneration;

import org.eatsy.appservice.model.RecipeModel;

/**
 * Interface for recipe test data creation
 */
public interface RecipeModelDataFactory {

    /**
     * Uses the Java Faker library to create a RecipeModel object
     *
     * @param maxIngredientSetSize Max value for the generated number of ingredients in the recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in the recipe
     * @return a randomly generated RecipeModel object.
     */
    RecipeModel generateRandomRecipeModel(int maxIngredientSetSize, int maxMethodMapSize);

}
