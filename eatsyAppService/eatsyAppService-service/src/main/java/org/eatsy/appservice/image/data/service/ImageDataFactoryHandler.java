package org.eatsy.appservice.image.data.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.RecipeImage;
import org.eatsy.appservice.image.mappers.ImageMapper;
import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.image.service.EatsyRecipeImageRepositoryService;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.stereotype.Component;

@Component
public class ImageDataFactoryHandler implements ImageDataFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //Recipe Mapper implementation
    private final ImageMapper imageMapperHandler;

    //Repository handler for recipe image persistence
    private final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler;

    //Inject the dependency of the recipeMapper and repositoryHandler implementations into the RecipeFactoryHandler during instantiation.
    public ImageDataFactoryHandler(final ImageMapper imageMapperHandler, final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler) {
        this.imageMapperHandler = imageMapperHandler;
        this.eatsyRecipeImageRepositoryHandler = eatsyRecipeImageRepositoryHandler;
    }

    @Override
    public byte[] retrieveImage(final String key) {
        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);
        final byte[] recipeImage = recipeImageEntity.getPicByte();
        return recipeImage;
    }

    @Override
    public ImageModel getImageData(final String key) {
        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);
        final RecipeImage recipeImage = imageMapperHandler.mapEntityToDomain(recipeImageEntity);
        final ImageModel imageModel = imageMapperHandler.mapDomanToModel(recipeImage);
        return imageModel;
    }
}
