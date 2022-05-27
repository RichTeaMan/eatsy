package org.eatsy.appservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The domain object for a Recipe to hold its information such as method, ingredients and name.
 */

//Lombok
@Getter
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


    private Recipe(final RecipeBuilder builder) {
        key = builder.key;
        name = builder.name;
        ingredientSet = builder.ingredientSet;
        method = builder.method;
    }

    /**
     * Builder for the Recipe Object.
     */
    /*
    Using a builder as there are more than a handful of parameters (this will likely expand).
    This will help avoid multiple constructors and helps control which fields are required for a Recipe on creation and which are not.

    The returned Recipe object is immutable. The builder setter methods return the builder object
    so are able to be chained for easy to read and write code.

    Static as we do not want an instance of the Builder.
     */
    public static class RecipeBuilder {

        //Required
        private final String key;
        private final String name;

        //Optional
        private Set<String> ingredientSet;
        private Map<Integer, String> method;

        //Constructor for the mandatory Recipe object fields
        public RecipeBuilder(final String name) {
            this.key = UUID.randomUUID().toString();
            this.name = name;
        }

        public RecipeBuilder withIngredientSet(final Set<String> ingredientSet) {
            this.ingredientSet = ingredientSet;
            return this;
        }

        public RecipeBuilder withMethod(final Map<Integer, String> method) {
            this.method = method;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }


    }
}
