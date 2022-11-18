package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Recipe Factory implementation
 * Tagged with @Component for dependency injection
 */
@Component
public class RecipeFactoryHandler implements RecipeFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //Cache of recipes
    private final Map<String, Recipe> recipeCache = new LinkedHashMap<>();

    //Recipe Mapper implementation
    private final RecipeMapper recipeMapperHandler;

    //Repository handler for persistence
    private final EatsyRepositoryService eatsyRepositoryHandler;

    //Inject the dependency of the recipeMapper and repositoryHandler implementations into the RecipeFactoryHandler during instantiation.
    public RecipeFactoryHandler(final RecipeMapper recipeMapperHandler, final EatsyRepositoryService eatsyRepositoryHandler) {
        this.recipeMapperHandler = recipeMapperHandler;
        this.eatsyRepositoryHandler = eatsyRepositoryHandler;
    }

    /**
     * Creates and persists a new Recipe.
     *
     * @param recipeModel the recipe model that has the data for the new Recipe
     * @return a recipe model object containing the data from the newly created and persisted recipe.
     */
    @Override
    public RecipeModel createRecipe(final RecipeModel recipeModel) {

        RecipeModel newRecipeModel = null;

        //The recipeModel to create a Recipe object must not be null and the recipeModel must have a recipeName.
        if (null != recipeModel && StringUtils.isNotEmpty(recipeModel.getName().trim())) {

            logger.debug("Creating a new recipe domain object called " + recipeModel.getName());

            final Recipe recipe = recipeMapperHandler.mapModelToDomain(recipeModel);

            //Persist the recipe to the database and add the new domain recipe to the cache of recipes.
            persistRecipeAndUpdateRecipeCache(recipe);

            newRecipeModel = recipeMapperHandler.mapDomainToModel(recipe);

        }
        return newRecipeModel;
    }

    /**
     * Retrieves all recipe model objects.
     *
     * @return The list of all recipe model objects that exist.
     */
    @Override
    public List<RecipeModel> retrieveAllRecipes() {

        logger.debug("Retrieving all recipes to return to the controller");

        //Retrieve all RecipeEntity objects from the database.
        final List<RecipeEntity> allRecipeEntities = eatsyRepositoryHandler.retrieveAllRecipes();

        //Update the domain in-memory recipeCache, and create a recipeModel list of all existing recipes to be returned to the controller.
        final List<RecipeModel> allRecipesModel = updateRecipeCacheAndGetAllRecipeModels(allRecipeEntities);

        return allRecipesModel;

    }

    /**
     * Deletes the requested recipeModel
     *
     * @param recipeKey of the recipe model that will be deleted from the recipe book
     * @return the list of existing recipe models that will have been updated to remove recipeModelToDelete
     */
    @Override
    public List<RecipeModel> deleteRecipe(final String recipeKey) {

        logger.debug("deleting recipe with key : " + recipeKey);

        //Delete the recipe with the specified recipeKey from the database.
        eatsyRepositoryHandler.deleteRecipeById(recipeKey);

        //Remove the recipe for deletion from the recipe cache.
        recipeCache.remove(recipeKey);

        //map the updated recipeCache to a recipeModel list to be returned.
        final List<RecipeModel> allRecipesModel = retrieveAllRecipeDomainsAndMapToModel();

        return allRecipesModel;
    }

    /**
     * Replaces the existing recipe with the updated version supplied.
     *
     * @param recipeKey              the unique ID of the recipe. This will allow the recipe that needs to be
     *                               updated to be identified.
     * @param recipeModelWithUpdates the recipe model with the updated changes to be persisted.
     * @return the updated recipeModel with the new updates/changes applied.
     */
    @Override
    public RecipeModel updateRecipe(final String recipeKey, final RecipeModel recipeModelWithUpdates) {

        logger.debug("replacing recipe with key: " + recipeKey + " for the new updated version");

        //Create the updated Recipe domain object
        final Recipe updatedRecipe = recipeMapperHandler.mapModelToDomain(recipeModelWithUpdates);

        //Persist the updated recipe
        logger.debug("Creating a corresponding recipe entity object for persistence called " + updatedRecipe.getName());
        final RecipeEntity recipeEntityWithUpdates = recipeMapperHandler.mapDomainToEntity(updatedRecipe);
        eatsyRepositoryHandler.persistRecipe(recipeEntityWithUpdates);

        //replace the outdated recipe with the updated version in the recipeCache.
        recipeCache.replace(recipeKey, updatedRecipe);
        //The updatedRecipe domain object has a new key generated on creation, so the Maps Key value will need to be updated to correspond.
        recipeCache.put(updatedRecipe.getKey(), recipeCache.remove(recipeKey));

        //Map the updated recipe to a RecipeModel and return.
        final RecipeModel updatedRecipeModel = recipeMapperHandler.mapDomainToModel(updatedRecipe);
        return updatedRecipeModel;
    }

    /**
     * Persist the recipe object to the database and update in-memory cache.
     *
     * @param recipe the recipe domain object to be persisted.
     */
    private void persistRecipeAndUpdateRecipeCache(final Recipe recipe) {

        logger.debug("Creating a new recipe entity object for persistence called " + recipe.getName());
        final RecipeEntity recipeEntity = recipeMapperHandler.mapDomainToEntity(recipe);

        //Persist the recipe to the database.
        final RecipeEntity persistedRecipeEntity = eatsyRepositoryHandler.persistRecipe(recipeEntity);

        //Add the new domain recipe to the cache of recipes.
        recipeCache.put(recipe.getKey(), recipe);

    }

    /**
     * Updates the in-memory domain Recipe cache and creates a list of all Recipe Model objects to be returned to the controller.
     *
     * @param allRecipeEntities the list of all recipe entities that exist in the recipe database table.
     * @return a list of all recipe model objects created from all recipe entities in the database.
     */
    private List<RecipeModel> updateRecipeCacheAndGetAllRecipeModels(final List<RecipeEntity> allRecipeEntities) {

        logger.debug("Updating in-memory domain recipe cache");

        //a recipeModel list to be returned to the controller when all existing recipes have been added to this list.
        final List<RecipeModel> allRecipesModel = new ArrayList<>();

        //Update the domain model in memory recipeCache to be up-to-date.
        for (final RecipeEntity currentRecipeEntity : allRecipeEntities) {

            final Recipe currentDomainRecipe = recipeMapperHandler.mapEntityToDomain(currentRecipeEntity);

            //If the map already contains a mapping for the key then the corresponding recipe (value) will be updated in the recipe cache.
            //If the key does is not already in the map, a new recipe entry will be added to the recipe cache.
            recipeCache.put(currentDomainRecipe.getKey(), currentDomainRecipe);

            //map the updated recipeCache to a recipeModel list to be returned to the controller.
            final RecipeModel currentModelRecipe = recipeMapperHandler.mapDomainToModel(currentDomainRecipe);
            allRecipesModel.add(currentModelRecipe);
        }

        logger.debug("returning the list of all recipe model objects.");

        return allRecipesModel;

    }

    /**
     * Map the updated recipeCache to a recipeModel list to be returned.
     *
     * @return all recipe models.
     */
    private List<RecipeModel> retrieveAllRecipeDomainsAndMapToModel() {
        final List<RecipeModel> allRecipesModel = new ArrayList();
        recipeCache.forEach((key, value) -> {
                    allRecipesModel.add(recipeMapperHandler.mapDomainToModel(value));
                }
        );
        return allRecipesModel;
    }
}


