package org.eatsy.appservice.testdatageneration;

import org.eatsy.appservice.model.RecipeModel;

import java.util.List;

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

    /**
     * Uses the Java Faker library to create a list of RecipeModel objects
     *
     * @param maxNumberOfRecipes Max value for the generated number of recipeModels in the list.
     * @param maxIngredientSetSize Max value for the generated number of ingredients in each recipe
     * @param maxMethodMapSize     Max value for the generated number of method steps in each recipe
     * @return a randomly generated list of recipeModel objects.
     */
    List<RecipeModel> generateRecipeModelsList(int maxNumberOfRecipes, int maxIngredientSetSize, int maxMethodMapSize);

}
