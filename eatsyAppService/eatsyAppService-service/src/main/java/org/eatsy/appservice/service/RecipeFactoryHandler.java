package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
    private final Map<String, Recipe> recipeCache = new HashMap<>();
    //Recipe Mapper implementation
    private RecipeMapper recipeMapperHandler;

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
     * @return The model object that has been created detailing all recipes.
     */
    @Override
    public Map<String, RecipeModel> retrieveAllRecipes() {

        Map<String, RecipeModel> allRecipesModel = new HashMap<>();
        for (final Recipe currentRecipe : recipeCache.values()) {
            allRecipesModel.put(currentRecipe.getKey(), recipeMapperHandler.mapToModel(currentRecipe));
        }
        //TODO Map all keys to model object. ^Fix key assignment so it all comes from model
        //TODO update unit tests
        return allRecipesModel;

    }
}
