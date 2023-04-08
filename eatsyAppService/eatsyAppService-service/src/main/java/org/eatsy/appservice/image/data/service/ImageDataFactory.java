package org.eatsy.appservice.image.data.service;

import org.eatsy.appservice.persistence.model.RecipeImageEntity;

public interface ImageDataFactory {

    byte[] retrieveImage(String key);

    RecipeImageEntity getImageData(String key);

}
