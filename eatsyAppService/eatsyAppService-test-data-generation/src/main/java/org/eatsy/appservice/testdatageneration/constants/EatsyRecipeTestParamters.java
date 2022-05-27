package org.eatsy.appservice.testdatageneration.constants;

/**
 * Constants for test class parameters
 */
public class EatsyRecipeTestParamters {

    private static final String API = "/api";

    public static final String ADD_RECIPE = API + "/add";
    public static final String RETRIEVE_ALL_RECIPES = API + "/retrieveAllRecipes";
    public static final String DELETE_RECIPE = API + "/deleteRecipe?recipeKey={key}";
    public static final String EDIT_RECIPE = API + "/edit/";

    public final static int MAX_INGREDIENT_SET_SIZE = 20;
    public final static int MAX_METHOD_MAP_SIZE = 10;
    public final static int MAX_NUMBER_OF_RECIPES = 15;

}
