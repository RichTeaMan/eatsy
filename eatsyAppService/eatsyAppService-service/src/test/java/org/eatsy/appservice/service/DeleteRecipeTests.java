package org.eatsy.appservice.service;

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
import java.util.UUID;

/**
 * Recipe Factory unit tests for the Delete Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DeleteRecipeTests {

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
     * Check the recipe factory correctly calls for the specified recipe to be deleted in the persistence service
     * and returns the updated list of all recipes
     * <p>
     */
    @Test
    public void checkDeleteForSpecifiedRecipe() {

        //Setup
        //In order to test recipe deletion, recipes must be pre-existing therefore recipes will be returned via the mocks as part of the setup
        final List<RecipeModel> inputRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Add unique key to each recipeModel in the list
        inputRecipeModelList.forEach(currentRecipeModel -> currentRecipeModel.setKey(UUID.randomUUID().toString()));

        //Select a recipe in the list for deletion at random
        final int randomListIndex = (int) ((Math.random() * inputRecipeModelList.size()));
        final RecipeModel recipeModelToBeDeleted = inputRecipeModelList.get(randomListIndex);
        final String uniqueKeyOfRecipeToDelete = recipeModelToBeDeleted.getKey();

        //Mock
        final List<RecipeModel> updatedRecipeModelListAfterDeletion = new ArrayList<>(inputRecipeModelList);
        updatedRecipeModelListAfterDeletion.remove(randomListIndex);
        final List<RecipeEntity> expectedRecipeEntityList = RecipeMockFactory.createMockRecipeEntityList(updatedRecipeModelListAfterDeletion);
        Mockito.when(eatsyRepositoryHandler.retrieveAllRecipes()).thenReturn(expectedRecipeEntityList);

        //Mock the response for each time the service under test calls the mapper layer recipeMapperHandler.mapEntityToDomain(testEntity) method for a given test entity.
        final List<Recipe> expectedRecipeList = RecipeMockFactory.createMockDomainRecipesFromEntityRecipes(expectedRecipeEntityList);
        for (final RecipeEntity currentEntity : expectedRecipeEntityList) {
            final Recipe expectedUpdatedDomainRecipe = RecipeMockFactory.createMockRecipe(currentEntity);
            Mockito.when(recipeMapperHandler.mapEntityToDomain(currentEntity)).thenReturn(expectedUpdatedDomainRecipe);
        }
        //Mock the response for each time the service under test calls the mapper layer recipeMapperHandler.mapDomainToModel(testRecipe) method for a given test entity.
        for (final Recipe currentRecipe : expectedRecipeList) {
            final RecipeModel expectedRecipeModel = RecipeMockFactory.createMockRecipeModelFromDomain(currentRecipe);
            Mockito.when(recipeMapperHandler.mapDomainToModel(currentRecipe)).thenReturn(expectedRecipeModel);
        }

        // Test
        // Delete the recipe
        final List<RecipeModel> actualUpdatedRecipeModelList = recipeFactoryHandler.deleteRecipeAndReturnUpdatedRecipeList(uniqueKeyOfRecipeToDelete);

        //Assert - check the recipe requested for deletion is no longer in the list of recipes, Check all the others are.
        //Confirm that the eatsyRepositoryHandler.deleteRecipeById(mockRecipeKey) gets called by the Service method under test one time
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).deleteRecipeById(uniqueKeyOfRecipeToDelete);
        //Confirm the retrieveAllRecipes method is called once and also its response returned by the method under test (deleteRecipeAndReturnUpdatedRecipeList)
        Mockito.verify(eatsyRepositoryHandler, Mockito.times(1)).retrieveAllRecipes();
        System.out.println(inputRecipeModelList);
        System.out.println(actualUpdatedRecipeModelList);
        Assertions.assertTrue(inputRecipeModelList.containsAll(actualUpdatedRecipeModelList));
        Assertions.assertEquals((inputRecipeModelList.size() - 1), actualUpdatedRecipeModelList.size());
        Assertions.assertFalse(actualUpdatedRecipeModelList.contains(recipeModelToBeDeleted));//check the recipe to be deleted is in-fact gone.
    }

}
