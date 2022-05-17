package org.eatsy.appservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The domain object for a Recipe to hold its information such as method, ingredients and name.
 */

//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class Recipe {

    //Unique identifier for recipe object
    private final String key;

    //Recipe name.
    private final String name;

    //The list of ingredients for the recipe
    private final Set<String> ingredientSet;

    //The method for creating the recipe from the ingredients
    private final Map<Integer, String> method;

    //Constructor
    public Recipe(final String name, final Set<String> ingredientSet, final Map<Integer, String> method) {
        this.key = UUID.randomUUID().toString();
        this.name = name;
        this.ingredientSet = ingredientSet;
        this.method = method;
    }



}
