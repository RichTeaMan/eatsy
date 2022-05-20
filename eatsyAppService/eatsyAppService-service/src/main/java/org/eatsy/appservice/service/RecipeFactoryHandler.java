package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
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

    //Inject the dependency of the recipeMapper implementation into the RecipeFactoryHandler during instantiation.
    public RecipeFactoryHandler(RecipeMapper recipeMapperHandler) {
        this.recipeMapperHandler = recipeMapperHandler;
    }

    /**
     * Creates a new recipe model object
     *
     * @param recipeModel the recipe model that will be used to create a recipe domain object
     * @return a recipe model object containing the data from the newly created recipe domain object
     */
    @Override
    public RecipeModel createRecipe(RecipeModel recipeModel) {

        RecipeModel newRecipeModel = null;

        //The recipeModel to create a Recipe object must not be null and the recipeModel must have a recipeName.
        if (null != recipeModel && StringUtils.isNotEmpty(recipeModel.getName().trim())) {

            logger.debug("Creating a new recipe domain object called " + recipeModel.getName());

            Recipe recipe = new Recipe(recipeModel.getName(), recipeModel.getIngredientSet(), recipeModel.getMethod());
            //Add the new recipe to the cache of recipes
            recipeCache.put(recipe.getKey(), recipe);

            newRecipeModel = recipeMapperHandler.mapToModel(recipe);

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

        List<RecipeModel> allRecipesModel = new ArrayList();
        for (final Recipe currentRecipe : recipeCache.values()) {
            allRecipesModel.add(recipeMapperHandler.mapToModel(currentRecipe));
        }

        return allRecipesModel;

    }

    /**
     * Deletes the requested recipeModel
     *
     * @param recipeKey of the recipe model that will be deleted from the recipe book
     * @return the list of existing recipe models that will have been updated to remove recipeModelToDelete
     */
    @Override
    public List<RecipeModel> deleteRecipe(String recipeKey) {

        logger.debug("deleting recipe with key : " + recipeKey);

        //Remove the recipe for deletion from the recipe cache.
        recipeCache.remove(recipeKey);

        //map the updated recipeCache to a recipeModel list to be returned.
        List<RecipeModel> allRecipesModel = new ArrayList();
        recipeCache.forEach((key, value) -> {
                    allRecipesModel.add(recipeMapperHandler.mapToModel(value));
                }
        );

        return allRecipesModel;
    }
}
