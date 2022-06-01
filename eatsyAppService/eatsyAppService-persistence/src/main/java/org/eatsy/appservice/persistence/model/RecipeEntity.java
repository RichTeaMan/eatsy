package org.eatsy.appservice.persistence.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Entity class to map the recipe object with a corresponding table in the database.
 */
//Lombok
@Getter
@Setter
@ToString
@EqualsAndHashCode
//persistence
@Entity
@Table(name = "recipe")
public class RecipeEntity {

    //Primary key
    @Id
    @Column(name = "key")
    private String key;

    //Recipe name.
    @Column(name = "name")
    private String name;

    //Defines a collection of instances.
    @ElementCollection
    //Join column used to map Recipe entity id (primary key value) to the Ingredients collection table's ID column.
    @CollectionTable(name = "ingredients", joinColumns = @JoinColumn(name = "key"))
    @Column(name = "ingredientSet")
    private Set<String> ingredientSet = new HashSet<>();


    @ElementCollection
    //The map's key is this column for our join table
    @MapKeyColumn(name = "method_step_number")
    //The Map's values corresponds to this column of the join table.
    @Column(name = "method_step")
    //Join column used to map Recipe entity id (primary key value) to the Ingredients collection table's ID column.
    @CollectionTable(name = "recipe_method", joinColumns = @JoinColumn(name = "key"))
    private Map<Integer, String> methodMap = new HashMap<>();

}
