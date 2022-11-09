package org.eatsy.appservice.service;

import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
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

import java.util.List;

/**
 * Recipe Factory unit tests for the Edit Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EditRecipeTests {

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
     * Check the Recipe Factory can edit an existing recipe.
     *
     * In order to test editing recipes, recipes must be pre-existing in the recipe cache, therefore recipes will be created as part of the setup.
     * Therefore, this test has a dependency on the CreateRecipe method in the service under test (RecipeFactoryHandler)
     */
    @Test
    public void checkEditRecipeNameSuccess() {

        //In order to test editing/updating, recipes must be pre-existing in the recipe cache, therefore recipes will be created as part of the setup

        //1) Add recipes to the cache as setup for the test (so editing can occur)
        final List<RecipeModel> inputRecipeModelWithKeysList = RecipeMockFactory.createRecipesInCache(
                recipeFactoryHandler, recipeMapperHandler, eatsyRepositoryHandler);

        //2) Get a new name to edit the recipe with
        final String newRecipeName = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE).getName();
        //Select a recipe in the list for the recipeName edit at random and get the key
        final int randomListIndex = (int) ((Math.random() * inputRecipeModelWithKeysList.size()));
        final String uniqueKeyOfRecipeToEdit = inputRecipeModelWithKeysList.get(randomListIndex).getKey();
        //Expected - What we expect the recipeModel to be after the name update (also what we will be submitting to the service as the updated model)
        final RecipeModel expectedUpdatedRecipeModel = inputRecipeModelWithKeysList.get(randomListIndex);
        expectedUpdatedRecipeModel.setName(newRecipeName);

        //3) Update the Mock for the mapper service to return the domain recipe with the updated name (whilst keeping the existing key - to correspond with the mocks.
        final Recipe mockedDomainRecipe = RecipeMockFactory.createMockDomainRecipeWithSpecifiedKey(expectedUpdatedRecipeModel, uniqueKeyOfRecipeToEdit);
        Mockito.when(recipeMapperHandler.mapModelToDomain(expectedUpdatedRecipeModel)).thenReturn(mockedDomainRecipe);

        //4) Update the Mock for  the mapper service to return the corresponding model recipe with key and updated name.
        Mockito.when(recipeMapperHandler.mapDomainToModel(mockedDomainRecipe)).thenReturn(expectedUpdatedRecipeModel);

        //Test - edit one of the recipe names in the recipeCache (This returned recipeModel will have a new key)
        final RecipeModel actualUpdatedRecipeModel = recipeFactoryHandler.updateRecipe(uniqueKeyOfRecipeToEdit, expectedUpdatedRecipeModel);

        //Assert
        Assertions.assertEquals(expectedUpdatedRecipeModel, actualUpdatedRecipeModel);

    }

}
