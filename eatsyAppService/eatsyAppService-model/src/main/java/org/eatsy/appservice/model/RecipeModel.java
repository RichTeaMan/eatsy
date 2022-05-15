package org.eatsy.appservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;
import java.util.Set;

/**
 * Model for the recipe object
 */
@Schema(description = "Stores and transports recipe data")
public class RecipeModel {

    @Schema(description = "Recipe name.")
    private String name;

    @Schema(description = "The list of ingredients for the recipe.")
    private Set<String> ingredientSet;

    @Schema(description = "The method for creating the recipe from the ingredients.")
    private Map<Integer, String> method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getIngredientSet() {
        return ingredientSet;
    }

    public void setIngredientSet(Set<String> ingredientSet) {
        this.ingredientSet = ingredientSet;
    }

    public Map<Integer, String> getMethod() {
        return method;
    }

    public void setMethod(Map<Integer, String> method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RecipeModel that = (RecipeModel) o;

        return new EqualsBuilder().append(name, that.name).append(ingredientSet, that.ingredientSet).append(method, that.method).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(ingredientSet).append(method).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("ingredientSet", ingredientSet)
                .append("method", method)
                .toString();
    }
}
