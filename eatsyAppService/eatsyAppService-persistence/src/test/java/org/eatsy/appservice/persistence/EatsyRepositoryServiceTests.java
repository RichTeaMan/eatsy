package org.eatsy.appservice.persistence;

import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepository;
import org.eatsy.appservice.persistence.service.EatsyRepositoryHandler;
import org.eatsy.appservice.testdatageneration.RecipeEntityDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * EatsyRepository unit tests for the EatsyRepositoryHandler persistence service
 */

//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EatsyRepositoryServiceTests {

    //Tells Mockito to mock the eatsyRepository instance
    @Mock
    private EatsyRepository eatsyRepository;

    /**
     * Class under test
     */
    @InjectMocks
    private EatsyRepositoryHandler eatsyRepositoryHandler;

    @BeforeEach
    public void setup() {
        //Initialise the class under test and the mock objects upon initialisation of Junit tests.
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkPersistRecipe() {

        //Setup

        //1) Create a recipe entity to persist
        final RecipeEntity recipeEntity = RecipeEntityDataFactory.generateRandomRecipeEntity(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //2) Mock the eatsyRepository
        Mockito.when(eatsyRepository.save(recipeEntity)).thenReturn(recipeEntity);

        //Test
        final RecipeEntity persistedRecipeEntity = eatsyRepositoryHandler.persistRecipe(recipeEntity);

        //Assertions
        Assertions.assertEquals(persistedRecipeEntity, recipeEntity);
    }

}
