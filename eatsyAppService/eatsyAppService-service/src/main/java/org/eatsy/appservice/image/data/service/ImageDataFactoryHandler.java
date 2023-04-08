package org.eatsy.appservice.image.data.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.image.service.EatsyRecipeImageRepositoryService;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.stereotype.Component;

@Component
public class ImageDataFactoryHandler implements ImageDataFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //Recipe Mapper implementation
    private final RecipeMapper recipeMapperHandler;

    //Repository handler for recipe image persistence
    private final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler;

    //Inject the dependency of the recipeMapper and repositoryHandler implementations into the RecipeFactoryHandler during instantiation.
    public ImageDataFactoryHandler(final RecipeMapper recipeMapperHandler, final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler) {
        this.recipeMapperHandler = recipeMapperHandler;
        this.eatsyRecipeImageRepositoryHandler = eatsyRecipeImageRepositoryHandler;
    }

    @Override
    public byte[] retrieveImage(final String key) {
        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);
        final byte[] recipeImage = recipeImageEntity.getPicByte();
        return recipeImage;
    }

    @Override
    public RecipeImageEntity getImageData(final String key) {
        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);
        return recipeImageEntity;
    }
}
