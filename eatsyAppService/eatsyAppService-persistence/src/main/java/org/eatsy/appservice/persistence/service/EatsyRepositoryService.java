package org.eatsy.appservice.persistence.service;

import org.eatsy.appservice.persistence.model.RecipeEntity;

import java.util.List;

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

    /**
     * Retrieves all Recipe Entity objects that are stored in the Recipe table
     *
     * @return the list of all recipeEntity objects that are in the Recipe database table.
     */
    List<RecipeEntity> retrieveAllRecipes();

    /**
     * Deletes the Recipe Entity object that is stored in the database with the specified unique key.
     */
    void deleteRecipeById(String recipeKey);
}
