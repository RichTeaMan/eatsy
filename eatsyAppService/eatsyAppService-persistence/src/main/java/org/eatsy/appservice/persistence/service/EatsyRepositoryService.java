package org.eatsy.appservice.persistence.service;

import org.eatsy.appservice.persistence.model.RecipeEntity;

/**
 * Interface for interacting with the JPA repository and persisting data
 */
public interface EatsyRepositoryService {

    /**
     * Persists the RecipeEntity object to the database.
     *
     * @param recipeEntity the recipe to be persisted.
     * @return the recipeEntity that has been successfully persisted.
     */
    RecipeEntity persistNewRecipe(final RecipeEntity recipeEntity);

}
