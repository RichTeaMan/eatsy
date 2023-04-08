package org.eatsy.appservice.image.mappers;

import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;

/**
 * Image Mapper to map between image domain, entity and model objects.
 */
public interface ImageMapper {

    ImageModel mapEntityToModel(final RecipeImageEntity recipeImageEntity);

}
