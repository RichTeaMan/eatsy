package org.eatsy.appservice.testdatageneration;

import org.eatsy.appservice.model.RecipeModel;

/**
 * Interface for recipe test data creation
 */
public interface RecipeModelDataFactory {

    /**
     * Uses the Java Faker library to create a RecipeModel object
     *
     * @return a randomly generated RecipeModel object.
     */
    RecipeModel generateRandomRecipeModel();

}
