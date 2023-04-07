package org.eatsy.appservice.service;


import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;

import java.util.List;

/**
 * Interface for interacting with recipes
 */
public interface RecipeFactory {

    /**
     * Retrieves all recipe model objects.
     *
     * @return The list of all recipe model objects that exist.
     */
    List<RecipeModel> retrieveAllRecipes();

    /**
     * Creates and persists a new Recipe.
     *
     * @param recipeModel the recipe model that has the data for the new Recipe
     * @return a recipe model object containing the data from the newly created and persisted recipe.
     */
    RecipeModel createRecipe(RecipeModel recipeModel);

    /**
     * Deletes the requested recipeModel and returns the updated list of recipes
     *
     * @param recipeKey the ID for the recipe model that will be deleted from the recipe book
     * @return the list of existing recipe models that will have been updated to remove recipeKey
     */
    List<RecipeModel> deleteRecipeAndReturnUpdatedRecipeList(String recipeKey);

    /**
     * Replaces the existing recipe with the updated version supplied.
     *
     * @param recipeModelWithUpdates the recipe model with the updated changes to be persisted.
     * @param recipeKey              the unique ID of the recipe. This will allow the recipe that needs to be
     *                               updated to be identified.
     * @return the updated recipeModel with the new updates/changes applied.
     */
    RecipeModel updateRecipe(String recipeKey, RecipeModel recipeModelWithUpdates);

    RecipeImageEntity retrieveRecipeImageEntity(final String imageKey);
}
