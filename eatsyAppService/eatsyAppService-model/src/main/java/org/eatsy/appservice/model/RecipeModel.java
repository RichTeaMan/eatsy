package org.eatsy.appservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * Model for the recipe object
 */
//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Schema(description = "Stores and transports recipe data")
public class RecipeModel {

    @Schema(description = "unique identifier.")
    private String key;

    @Schema(description = "Recipe name.")
    private String name;

    @Schema(description = "The list of ingredients for the recipe.")
    private Set<String> ingredientSet;

    @Schema(description = "The method for creating the recipe from the ingredients.")
    private Map<Integer, String> method;

}
