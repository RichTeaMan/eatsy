package org.eatsy.appservice.model.mappers;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.springframework.stereotype.Component;

/**
 * Recipe Mapper Handler to map between recipe domain and model objects.
 */
@Component
public class RecipeMapperHandler implements RecipeMapper {

    private static final Logger logger = LogManager.getLogger(RecipeMapperHandler.class);

    /**
     * Map the recipe domain object to a recipe model object.
     *
     * @param recipe the domain object to be mapped
     * @return the recipeModel object that has been created from the recipe domain object.
     */
    @Override
    public RecipeModel mapToModel(final Recipe recipe) {

        RecipeModel recipeModel = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != recipe && StringUtils.isNotEmpty(recipe.getName().trim())) {

            logger.debug("Mapping domain object " + recipe.getName() + " to a recipeModel object");

            recipeModel = new RecipeModel();

            //Map key.
            recipeModel.setKey(recipe.getKey());

            //Map name.
            recipeModel.setName(recipe.getName());

            //Map set of ingredients.
            recipeModel.setIngredientSet(recipe.getIngredientSet());

            //Map method.
            recipeModel.setMethod(recipe.getMethod());

        }
        return recipeModel;

    }

    /**
     * Map the recipeModel to a recipe domain object.
     *
     * @param recipeModel the model object to be mapped to domain object
     * @return the recipe domain object that has been created from the recipe model object
     */
    @Override
    public Recipe mapToDomain(final RecipeModel recipeModel) {

        Recipe recipe = null;
        //The recipe model to be mapped must not be null and the recipeModel must have a name.
        if (null != recipeModel && StringUtils.isNotEmpty(recipeModel.getName().trim())) {

            logger.debug("Mapping model object " + recipeModel.getName() + " to a recipe domain object");

            recipe = new Recipe.RecipeBuilder(recipeModel.getName())
                    .withIngredientSet(recipeModel.getIngredientSet())
                    .withMethod(recipeModel.getMethod())
                    .build();

        }

        return recipe;

    }
}
