package org.eatsy.appservice.image.data.service;

import org.eatsy.appservice.model.ImageModel;

public interface ImageDataFactory {

    byte[] retrieveImage(String key);

    ImageModel getImageData(String key);


}
