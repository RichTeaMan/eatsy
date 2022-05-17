package org.eatsy.appservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * The domain object for a Recipe to hold its information such as method, ingredients and name.
 */

//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class Recipe {

    //Recipe name.
    private final String name;

    //The list of ingredients for the recipe
    private Set<String> ingredientSet;

    //The method for creating the recipe from the ingredients
    private Map<Integer, String> method;

    //Constructor
    public Recipe(String name, Set<String> ingredientSet, Map<Integer, String> method) {
        this.name = name;
        this.ingredientSet = ingredientSet;
        this.method = method;
    }

}
