package org.eatsy.appservice.model.mappers;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.persistence.model.RecipeEntity;
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
    public RecipeModel mapDomainToModel(final Recipe recipe) {

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
            recipeModel.setIngredients(recipe.getIngredients());

            //Map method.
            recipeModel.setMethod(recipe.getMethod());

        }
        return recipeModel;

    }

    /**
     * Map the recipeModel to a recipe domain object.
     * If the model has an existing key, the mapper ensures the existing key is kept.
     * If the model doesn't yet have a key, then a new key will be assigned.
     *
     * @param recipeModel the model object to be mapped to domain object
     * @return the recipe domain object that has been created from the recipe model object
     */
    @Override
    public Recipe mapModelToDomain(final RecipeModel recipeModel) {

        Recipe recipe = null;
        //The recipe model to be mapped must not be null, the recipeModel must have a name, an uploader and a recipe summary
        if (null != recipeModel
                && StringUtils.isNotEmpty(recipeModel.getName().trim())
                && StringUtils.isNotEmpty(recipeModel.getUploader().trim())
                && StringUtils.isNotEmpty(recipeModel.getRecipeSummary().trim())) {

            logger.debug("Mapping model object " + recipeModel.getName() + " to a recipe domain object");

            final Recipe.RecipeBuilder recipeBuilder = new Recipe.RecipeBuilder(
                    recipeModel.getName(), recipeModel.getUploader(), recipe.getRecipeSummary())
                    .withIngredients(recipeModel.getIngredients())
                    .withMethod(recipeModel.getMethod())
                    .withTags(recipeModel.getTags());
            // The recipeBuilder automatically assigns a new key,
            // so if the model already has an existing key, then this will ensure the existing key is kept.
            if (recipeModel.getKey() != null) {
                recipeBuilder.withSpecifiedKey(recipeModel.getKey());
            }

            recipe = recipeBuilder.build();

        }

        return recipe;

    }

    /**
     * Map the recipe domain object to a recipe entity object for persistence to database.
     *
     * @param recipe the domain object to be mapped
     * @return the recipeEntity object that has been created from the recipe domain object.
     */
    @Override
    public RecipeEntity mapDomainToEntity(final Recipe recipe) {

        RecipeEntity recipeEntity = null;
        //The recipe to be mapped must not be null and the recipe must have a name, uploader and summary.
        if (null != recipe
                && StringUtils.isNotEmpty(recipe.getName().trim())
                && StringUtils.isNotEmpty(recipe.getUploader().trim())
                && StringUtils.isNotEmpty(recipe.getRecipeSummary().trim())) {

            logger.debug("Mapping domain object " + recipe.getName() + " to a recipeEntity object");

            recipeEntity = new RecipeEntity();

            //Map key.
            recipeEntity.setKey(recipe.getKey());

            //Map name.
            recipeEntity.setName(recipe.getName());

            //Map uploader.
            recipeEntity.setUploader(recipeEntity.getUploader());

            //Map recipeSummary.
            recipeEntity.setRecipeSummary(recipeEntity.getRecipeSummary());

            //Map thumbsUpCount.
            recipeEntity.setThumbsUpCount(recipe.getThumbsUpCount());

            //Map thumbsDownCount.
            recipeEntity.setThumbsDownCount(recipe.getThumbsDownCount());

            //Map tags.
            recipeEntity.setTags(recipe.getTags());

            //Map set of ingredients.
            recipeEntity.setIngredientsMap(recipeEntity.getIngredientsMap());

            //Map method.
            recipeEntity.setMethodMap(recipe.getMethod());

        }
        return recipeEntity;
    }

    /**
     * Map the recipe entity object to a recipe domain object
     *
     * @param recipeEntity the entity object to be mapped
     * @return the recipeDomain object that has been created from the recipe entity object
     */
    @Override
    public Recipe mapEntityToDomain(final RecipeEntity recipeEntity) {

        Recipe recipe = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != recipeEntity
                && StringUtils.isNotEmpty(recipeEntity.getName().trim())
                && StringUtils.isNotEmpty(recipeEntity.getUploader().trim())
                && StringUtils.isNotEmpty(recipeEntity.getRecipeSummary().trim())) {

            logger.debug("Mapping entity object " + recipeEntity.getName() + " to a recipedomain object");

            recipe = new Recipe
                    .RecipeBuilder(recipeEntity.getName(), recipeEntity.getUploader(), recipeEntity.getRecipeSummary())
                    .withTags(recipeEntity.getTags())
                    .withIngredients(recipeEntity.getIngredientsMap())
                    .withMethod(recipeEntity.getMethodMap())
                    .withSpecifiedKey(recipeEntity.getKey()) //override the newly generated key to ensure it is the same as the db entity key
                    .build();

        }
        return recipe;

    }
}
