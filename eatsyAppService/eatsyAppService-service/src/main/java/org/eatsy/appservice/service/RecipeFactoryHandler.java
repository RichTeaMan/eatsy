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
import java.util.List;

/**
 * Recipe Factory implementation
 * Tagged with @Component for dependency injection
 */
@Component
public class RecipeFactoryHandler implements RecipeFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

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
        if (null != recipeModel
                && StringUtils.isNotEmpty(recipeModel.getName().trim())
                && StringUtils.isNotEmpty(recipeModel.getUploader().trim())
                && StringUtils.isNotEmpty(recipeModel.getRecipeSummary().trim())) {

            logger.debug("Creating a new recipe domain object called " + recipeModel.getName());

            final Recipe recipe = recipeMapperHandler.mapModelToDomain(recipeModel);

            logger.debug("Creating a new recipe entity object for persistence called " + recipe.getName());
            final RecipeEntity recipeEntity = recipeMapperHandler.mapDomainToEntity(recipe);

            //Persist the recipe to the database.
            final RecipeEntity persistedRecipeEntity = eatsyRepositoryHandler.persistRecipe(recipeEntity);

            final Recipe persistedRecipeDomain = recipeMapperHandler.mapEntityToDomain(persistedRecipeEntity);

            newRecipeModel = recipeMapperHandler.mapDomainToModel(persistedRecipeDomain);

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

        //Create a recipeModel list of all existing recipes to be returned to the controller
        //by mapping the recipeEntities to recipeModels via the domain model.
        final List<RecipeModel> allRecipesModel = createRecipeModelList(allRecipeEntities);

        return allRecipesModel;

    }

    /**
     * Deletes the requested recipe and returns the updated list of recipes via the model
     *
     * @param recipeKey of the recipe model that will be deleted from the recipe book
     * @return the list of existing recipe models that will have been updated to remove recipeModelToDelete
     */
    @Override
    public List<RecipeModel> deleteRecipeAndReturnUpdatedRecipeList(final String recipeKey) {

        logger.debug("deleting recipe with key : " + recipeKey);

        //Delete the recipe with the specified recipeKey from the database.
        eatsyRepositoryHandler.deleteRecipeById(recipeKey);


        logger.debug("returning the updated list of all recipes via the model");
        final List<RecipeModel> allRecipesModel = retrieveAllRecipes();

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

        final RecipeEntity persistedEntity = eatsyRepositoryHandler.persistRecipe(recipeEntityWithUpdates);

        //Map the updated recipe to a RecipeModel and return.
        final Recipe PersistedRecipeDomain = recipeMapperHandler.mapEntityToDomain(persistedEntity);
        final RecipeModel updatedRecipeModel = recipeMapperHandler.mapDomainToModel(PersistedRecipeDomain);
        return updatedRecipeModel;
    }

    /**
     * Creates a list of all Recipe Model objects to be returned to the controller from all RecipeEntites in the database.
     *
     * @param allRecipeEntities the list of all recipe entities that exist in the recipe database table.
     * @return a list of all recipe model objects created from all recipe entities in the database.
     */
    private List<RecipeModel> createRecipeModelList(final List<RecipeEntity> allRecipeEntities) {

        logger.debug("Map the recipeEntites to domain objects, before creating and mapping to a recipeModel list");

        //a recipeModel list to be returned to the controller when all existing recipes have been added to this list.
        final List<RecipeModel> allRecipesModel = new ArrayList<>();

        //Map recipeEntities to the domain model.
        for (final RecipeEntity currentRecipeEntity : allRecipeEntities) {

            final Recipe currentDomainRecipe = recipeMapperHandler.mapEntityToDomain(currentRecipeEntity);

            //map the domain model to a recipeModel list to be returned to the controller.
            final RecipeModel currentModelRecipe = recipeMapperHandler.mapDomainToModel(currentDomainRecipe);
            allRecipesModel.add(currentModelRecipe);
        }

        logger.debug("returning the list of all recipe model objects.");

        return allRecipesModel;

    }

}


