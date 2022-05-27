package org.eatsy.appservice.model.mappers;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;

/**
 * Recipe Mapper to map between recipe domain and model objects.
 */
public interface RecipeMapper {

    /**
     * Map the recipe domain object to a recipe model object.
     *
     * @param recipe the domain object to be mapped
     * @return the recipeModel object that has been created from the recipe domain object.
     */
    RecipeModel mapToModel(final Recipe recipe);

    /**
     * Map the recipeModel to a recipe domain object.
     *
     * @param recipeModel the model object to be mapped to domain object
     * @return the recipe domain object that has been created from the recipe model object
     */
    Recipe mapToDomain(final RecipeModel recipeModel);

}
