package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.eatsy.appservice.domain.Recipe;
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
    private RecipeFactoryHandler recipeFactoryHandler;

    @BeforeEach
    public void setup() {
        //Initialise the mock objects upon initialisation of Junit tests.
        MockitoAnnotations.openMocks(this);
        //Initialise the class under test and inject the mocks.
        recipeFactoryHandler = new RecipeFactoryHandler(recipeMapperHandler, eatsyRepositoryHandler);
    }

    /**
     * Check the recipe factory correctly returns all recipes
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup and mocking

        //Create the expected list of recipes to be retrieved
        final List<RecipeModel> expectedRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Mock eatsyRepositoryHandler interactions
        final List<RecipeEntity> expectedRecipeEntityList = createMockRecipeEntity(expectedRecipeModelList);
        Mockito.when(eatsyRepositoryHandler.retrieveAllRecipes()).thenReturn(expectedRecipeEntityList);

        //Mock recipeMapperHandler service for each of the recipe entities that the mapper service will externally map to domain objects.
        for (final RecipeEntity currentRecipeEntity : expectedRecipeEntityList) {
            final Recipe currentDomainRecipe = mappedTestEntityToDomain(currentRecipeEntity);
            Mockito.when(recipeMapperHandler.mapEntityToDomain(currentRecipeEntity)).thenReturn(currentDomainRecipe);

            //Mock recipeMapperHandler service for each of the mapped domain objects that will externally be mapped to model objects
            Mockito.when(recipeMapperHandler.mapDomainToModel(currentDomainRecipe)).thenReturn(mappedTestDomainRecipeToModel(currentDomainRecipe));
        }

        //Test

        final List<RecipeModel> actualRecipeModelsList = recipeFactoryHandler.retrieveAllRecipes();

        //Assert

        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }

    /**
     * A test method to facilitate the "retrieve all recipe" unit test by mocking part of the external mapper service functionality.
     * Map the recipe domain object to a recipe model object.
     *
     * @param currentDomainRecipe the domain object to be mapped
     * @return the recipeModel object that has been created from the recipe domain object.
     */
    private RecipeModel mappedTestDomainRecipeToModel(final Recipe currentDomainRecipe) {
        final RecipeModel recipeModel = new RecipeModel();

        //Map key.
        recipeModel.setKey(currentDomainRecipe.getKey());

        //Map name.
        recipeModel.setName(currentDomainRecipe.getName());

        //Map set of ingredients.
        recipeModel.setIngredientSet(currentDomainRecipe.getIngredientSet());

        //Map method.
        recipeModel.setMethod(currentDomainRecipe.getMethod());

        return recipeModel;
    }

    /**
     * A test method to facilitate the "retrieve all recipe" unit test by mocking part of the external mapper service functionality.
     * Map the recipe entity object to a recipe domain object
     *
     * @param currentRecipeEntity the entity object to be mapped
     * @return the recipeDomain object that has been created from the recipe entity object
     */
    private Recipe mappedTestEntityToDomain(final RecipeEntity currentRecipeEntity) {

        final Recipe recipe = new Recipe
                .RecipeBuilder(currentRecipeEntity.getName())
                .withIngredientSet(currentRecipeEntity.getIngredientSet())
                .withMethod(currentRecipeEntity.getMethodMap())
                .withSpecifiedKey(currentRecipeEntity.getKey()) //override the newly generated key to ensure it is the same as the db entity key
                .build();

        return recipe;
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
