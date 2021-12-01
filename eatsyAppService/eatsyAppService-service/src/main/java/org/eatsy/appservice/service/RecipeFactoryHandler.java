package org.eatsy.appservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.springframework.stereotype.Service;

/**
 * Recipe Factory implementation
 * Tagged with @Service for dependency injection
 */
@Service
public class RecipeFactoryHandler implements RecipeFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

    /**
     * Creates a new recipe object
     *
     * @param recipeName the name of the recipe being created
     * @return the new recipe object that has been created
     */
    @Override
    public Recipe createRecipe(String recipeName) {

        logger.debug("Creating a new recipe called " + recipeName);

        Recipe recipe = new Recipe(recipeName);
        return recipe;

    }
}
