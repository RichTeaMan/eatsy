package org.eatsy.appservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.springframework.stereotype.Component;

/**
 * Recipe Factory implementation
 * Tagged with @Component for dependency injection
 */
@Component
public class RecipeFactoryHandler implements RecipeFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

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

        logger.debug("Creating a new recipe domain object called " + recipeModel.getName());

        Recipe recipe = new Recipe(recipeModel.getName(), recipeModel.getIngredientSet(), recipeModel.getMethod());
        RecipeModel newRecipeModel = recipeMapperHandler.mapToModel(recipe);
        return newRecipeModel;
    }
}
