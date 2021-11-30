package org.eatsy.appservice.service;

import org.eatsy.appservice.domain.Recipe;

/**
 * Interface for interacting with recipes
 */
public interface RecipeFactory {

    /**
     * Creates a new recipe object
     *
     * @param recipeName the name of the recipe being created
     * @return the new recipe object that has been created
     */
    Recipe createRecipe(String recipeName);

}
