package org.eatsy.appservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Model for the recipe image object
 */
//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Schema(description = "Stores and transports recipe image data")
public class ImageModel {

    @Schema(description = "Primary key")
    private String key;

    @Schema(description = "Image name")
    private String imageName;

    @Schema(description = "type of file e.g image/jpeg")
    private String imageType;

}
