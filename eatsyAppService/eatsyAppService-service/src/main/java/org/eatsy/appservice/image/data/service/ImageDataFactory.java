package org.eatsy.appservice.image.data.service;

import org.eatsy.appservice.model.ImageModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


/**
 * Interface for interacting with RecipeImageData
 */
public interface ImageDataFactory {

    /**
     * Retrieves the image (in byte[] form) from the database
     *
     * @param key the ID of the image for a given recipe.
     * @return The image as it is stored in the database (byte[] format).
     */
    byte[] retrieveImage(String key);

    /**
     * Retrieves the image metadata for a given recipeImage
     *
     * @param key the ID of the image for a given recipe.
     * @return The image metadata, such as key, imageName and imageType (excluding the image itself).
     */
    ImageModel getImageData(String key);

    /**
     * Uploads the image and associated image metadata
     *
     * @param recipeKey the unique ID of the parent Recipe object that the image corresponds to.
     * @param fileSet the images to be uploaded for a given recipe
     * @return The ImageModel of the successfully uploaded file.
     */
    Set<ImageModel> uploadImages(String recipeKey, Set<MultipartFile> fileSet);
}
