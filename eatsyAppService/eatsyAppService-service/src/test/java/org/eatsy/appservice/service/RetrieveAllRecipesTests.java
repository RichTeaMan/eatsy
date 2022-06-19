package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe Factory unit tests for the Create Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RetrieveAllRecipesTests {

    //Create a mock implementation of the RecipeMapper. These unit tests are only concerned with the service module not the mapper module.
    @Mock
    private RecipeMapper recipeMapperHandler;

    //Create a mock implementation of the EatsyRepositoryService. These unit tests are only concerned with the service module not the persistence module.
    @Mock
    private EatsyRepositoryService eatsyRepositoryHandler;

    /**
     * Class under test.
     */
    //Injects the dependent mocks (marked with @Mock) for a recipe factory instance.
    @InjectMocks
    private RecipeFactory recipeFactory;

    @BeforeEach
    public void setup() {
        //Initialise the mock objects upon initialisation of Junit tests.
        MockitoAnnotations.openMocks(this);
        //Initialise the class under test and inject the mocks.
        recipeFactory = new RecipeFactoryHandler(recipeMapperHandler, eatsyRepositoryHandler);

    }

    /**
     * Check the recipe factory correctly returns all recipes
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup
        //Create the expected list of recipes to be retrieved
        final List<RecipeModel> expectedRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        final List<RecipeEntity> expectedRecipeEntityList = createMockRecipeEntity(expectedRecipeModelList);

        Mockito.when(eatsyRepositoryHandler.retrieveAllRecipes()).thenReturn(expectedRecipeEntityList);

        //TODO carry on unit test from here

        //Populate the recipeCache with the randomly generated recipes
        for (final RecipeModel currentRecipeModel : expectedRecipeModelList) {
            final RecipeModel createdRecipeModel = recipeFactory.createRecipe(currentRecipeModel);

            //To ensure the test doesn't fail on the randomly generated UUIDs.
            currentRecipeModel.setKey(createdRecipeModel.getKey());
        }

        //Test
        final List<RecipeModel> actualRecipeModelsList = recipeFactory.retrieveAllRecipes();

        //Assert
        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }

    /**
     * Creates a list of Recipe Entity objects from a list of Recipe Model objects
     *
     * @param inputRecipeModelList list recipe model object.
     * @return A list of Recipe Entity object.
     */
    private static List<RecipeEntity> createMockRecipeEntity(final List<RecipeModel> inputRecipeModelList) {

        final List<RecipeEntity> recipeEntityList = new ArrayList<>();

        for (final RecipeModel currentRecipeModel : inputRecipeModelList) {

            RecipeEntity recipeEntity = null;
            //The recipe to be mapped must not be null and the recipe must have a name.
            if (null != currentRecipeModel && StringUtils.isNotEmpty(currentRecipeModel.getName().trim())) {

                recipeEntity = new RecipeEntity();
                //Map key.
                recipeEntity.setKey(currentRecipeModel.getKey());
                //Map name.
                recipeEntity.setName(currentRecipeModel.getName());
                //Map set of ingredients.
                recipeEntity.setIngredientSet(currentRecipeModel.getIngredientSet());
                //Map method.
                recipeEntity.setMethodMap(currentRecipeModel.getMethod());

            }
            recipeEntityList.add(recipeEntity);

        }

        return recipeEntityList;
    }

}
