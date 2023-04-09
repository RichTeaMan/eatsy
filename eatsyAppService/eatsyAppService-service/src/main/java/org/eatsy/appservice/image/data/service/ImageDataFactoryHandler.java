package org.eatsy.appservice.image.data.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.domain.RecipeImage;
import org.eatsy.appservice.image.mappers.ImageMapper;
import org.eatsy.appservice.model.ImageModel;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.persistence.image.service.EatsyRecipeImageRepositoryService;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * RecipeImageData Factory implementation
 * Tagged with @Component for Spring dependency injection
 */
@Component
public class ImageDataFactoryHandler implements ImageDataFactory {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //RecipeImage Mapper implementation
    private final ImageMapper imageMapperHandler;

    //Repository handler for recipe image persistence
    private final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler;

    //Inject the dependency of the recipeImageMapper and repositoryImageHandler implementations into the RecipeImageDataFactoryHandler during instantiation.
    public ImageDataFactoryHandler(final ImageMapper imageMapperHandler, final EatsyRecipeImageRepositoryService eatsyRecipeImageRepositoryHandler) {
        this.imageMapperHandler = imageMapperHandler;
        this.eatsyRecipeImageRepositoryHandler = eatsyRecipeImageRepositoryHandler;
    }

    /**
     * Retrieves the image (in byte[] form) from the database
     *
     * @return The image as it is stored in the database (byte[] format).
     */
    @Override
    public byte[] retrieveImage(final String key) {

        logger.debug("Retrieving the requested recipeImageEntity");
        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);
        //Get and return the image byte[] from this recipeImageEntity
        final byte[] recipeImage = recipeImageEntity.getPicByte();
        return recipeImage;
    }

    /**
     * Retrieves the image metadata for a given recipeImage
     *
     * @param key the ID of the image for a given recipe.
     * @return The image metadata, such as key, imageName and imageType (excluding the image itself).
     */
    @Override
    public ImageModel getImageData(final String key) {

        logger.debug("Retrieving the recipeImageData to return to the controller");

        final RecipeImageEntity recipeImageEntity = eatsyRecipeImageRepositoryHandler.findImageByKey(key);

        //Map to domain to ensure entity recipeImage object is of allowed composition
        final RecipeImage recipeImage = imageMapperHandler.mapEntityToDomain(recipeImageEntity);

        //Map to model to be returned to the external service/API consumer via the controller.
        //This does not map the image byteArray as this is just method just returns the image metadata.
        final ImageModel imageModel = imageMapperHandler.mapDomanToModel(recipeImage);
        return imageModel;
    }

    /**
     * Uploads the image and associated image metadata
     *
     * @param recipeKey the unique ID of the parent Recipe object that the image corresponds to.
     * @param fileSet   the images to be uploaded for a given recipe
     * @return The ImageModel of the successfully uploaded file.
     */
    @Override
    public Set<ImageModel> uploadImages(final String recipeKey, final Set<MultipartFile> fileSet) throws IOException {

        logger.debug("");

        final Set<RecipeImage> recipeImageSet = new HashSet<>();

        for (final MultipartFile currentFile : fileSet) {
            final RecipeImage recipeImage = new RecipeImage.RecipeImageBuilder(
                    currentFile.getName(),
                    currentFile.getContentType(),
                    currentFile.getBytes()).build();

            recipeImageSet.add(recipeImage);

        }



    }
}
