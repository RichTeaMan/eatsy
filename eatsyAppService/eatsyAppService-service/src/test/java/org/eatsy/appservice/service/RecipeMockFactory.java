package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.mockito.Mockito;

import java.util.List;

/**
 * Interface for recipe test mockito mock creation utility methods.
 */
public interface RecipeMockFactory {

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services for a give list of input recipe models.
     *
     * @param inputRecipeModelList   randomly generated recipe model test data.
     * @param recipeMapperHandler    mock implementation of the RecipeMapper
     * @param eatsyRepositoryHandler mock implementation of the EatsyRepositoryService
     */
    static void createMocksForRecipeMapperAndEatsyRepositoryServices(
            final List<RecipeModel> inputRecipeModelList, final RecipeMapper recipeMapperHandler, final EatsyRepositoryService eatsyRepositoryHandler) {

        for (final RecipeModel currentInputRecipeModel : inputRecipeModelList) {
            createMocksForRecipeMapperAndEatsyRepositoryServices(currentInputRecipeModel, recipeMapperHandler, eatsyRepositoryHandler);
        }

    }

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services for a given input recipe model.
     *
     * @param inputRecipeModel       randomly generated recipe model test data.
     * @param recipeMapperHandler    mock implementation of the RecipeMapper
     * @param eatsyRepositoryHandler mock implementation of the EatsyRepositoryService
     */
    static void createMocksForRecipeMapperAndEatsyRepositoryServices(
            final RecipeModel inputRecipeModel, final RecipeMapper recipeMapperHandler, final EatsyRepositoryService eatsyRepositoryHandler) {

        //Configure the RecipeMapper mocks to return mocked data when it's mapper methods are called from the RecipeFactory.
        final Recipe mockedDomainRecipe = createMockDomainRecipe(inputRecipeModel); //unique key assigned.
        Mockito.when(recipeMapperHandler.mapModelToDomain(inputRecipeModel)).thenReturn(mockedDomainRecipe);

        //Configure the eatsyRepository Mock to return the mocked data (Recipe Entity) when the eatsyRepository is called.
        final RecipeEntity mockedPersistedRecipeEntity = createMockRecipeEntity(mockedDomainRecipe);
        Mockito.when(eatsyRepositoryHandler.persistNewRecipe(mockedPersistedRecipeEntity)).thenReturn(mockedPersistedRecipeEntity);
        Mockito.when(recipeMapperHandler.mapDomainToEntity(mockedDomainRecipe)).thenReturn(mockedPersistedRecipeEntity);

        //Configure the RecipeMapperMock to return the mocked data (with the unique keys in the cache) when the MapperService is called.
        final RecipeModel mockedCreatedRecipeModelWithKey = createMockRecipeModelFromDomain(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapDomainToModel(mockedDomainRecipe)).thenReturn(mockedCreatedRecipeModelWithKey);
    }

    /**
     * Creates a Recipe Model object from a Recipe Domain object that has been added to the recipeCache
     *
     * @param mockedDomainRecipe recipe domain object.
     * @return A Recipe Model object.
     */
    static RecipeModel createMockRecipeModelFromDomain(final Recipe mockedDomainRecipe) {

        RecipeModel recipeModel = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != mockedDomainRecipe && StringUtils.isNotEmpty(mockedDomainRecipe.getName().trim())) {

            recipeModel = new RecipeModel();
            //Map key.
            recipeModel.setKey(mockedDomainRecipe.getKey());
            //Map name.
            recipeModel.setName(mockedDomainRecipe.getName());
            //Map set of ingredients.
            recipeModel.setIngredientSet(mockedDomainRecipe.getIngredientSet());
            //Map method.
            recipeModel.setMethod(mockedDomainRecipe.getMethod());

        }
        return recipeModel;

    }

    /**
     * Creates a Recipe Entity object from a Recipe Domain object
     *
     * @param inputRecipeDomain recipe model object.
     * @return A Recipe Entity object.
     */
    static RecipeEntity createMockRecipeEntity(final Recipe inputRecipeDomain) {

        RecipeEntity recipeEntity = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeDomain && StringUtils.isNotEmpty(inputRecipeDomain.getName().trim())) {

            recipeEntity = new RecipeEntity();
            //Map key.
            recipeEntity.setKey(inputRecipeDomain.getKey());
            //Map name.
            recipeEntity.setName(inputRecipeDomain.getName());
            //Map set of ingredients.
            recipeEntity.setIngredientSet(inputRecipeDomain.getIngredientSet());
            //Map method.
            recipeEntity.setMethodMap(inputRecipeDomain.getMethod());

        }
        return recipeEntity;

    }

    /**
     * Creates a Recipe Domain object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Domain object.
     */
    static Recipe createMockDomainRecipe(final RecipeModel inputRecipeModel) {

        Recipe domainRecipe = null;
        //The recipe model to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())) {

            domainRecipe = new Recipe
                    .RecipeBuilder(inputRecipeModel.getName())
                    .withIngredientSet(inputRecipeModel.getIngredientSet())
                    .withMethod(inputRecipeModel.getMethod())
                    .build();

        }
        return domainRecipe;

    }

}
