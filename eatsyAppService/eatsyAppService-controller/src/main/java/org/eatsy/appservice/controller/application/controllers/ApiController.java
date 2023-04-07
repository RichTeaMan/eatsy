package org.eatsy.appservice.controller.application.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.controller.application.constants.EatsyRecipeEndpoints;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.persistence.model.RecipeImageEntity;
import org.eatsy.appservice.service.RecipeFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


/**
 * API Controller for creating, editing, deleting and retrieving recipes.
 * All handling methods on this controller are relative to the /api path.
 */
@RestController //Inform the DispatcherServlet that this class contains request mapping methods.
@RequestMapping(EatsyRecipeEndpoints.API)
@EnableAutoConfiguration
public class ApiController {

    //logger
    private static final Logger logger = LogManager.getLogger(ApiController.class);

    //Interface for recipe operations
    private final RecipeFactory recipeFactoryHandler;

    //Inject the dependency of the recipeFactory implementation into the api controller during instantiation.
    public ApiController(final RecipeFactory recipeFactoryHandler) {
        this.recipeFactoryHandler = recipeFactoryHandler;
    }


    /**
     * "Returns a new recipe with the information provided in the request"
     *
     * @param recipeModel The recipe the user is adding.
     * @return the recipe model object that has been created.
     */
    @Operation(description = "Returns a new recipe with the information provided in the request")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully created new recipe.")})
    @RequestMapping(value = EatsyRecipeEndpoints.ADD_RECIPE, method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<RecipeModel> addRecipe(
            @Parameter(description = "The recipe to be created.") @RequestBody final RecipeModel recipeModel) {

        logger.debug("A new request has been made to create a recipe called " + recipeModel.getName());
        final RecipeModel newRecipeModel = recipeFactoryHandler.createRecipe(recipeModel);

        final ResponseEntity<RecipeModel> response = new ResponseEntity<RecipeModel>(newRecipeModel, HttpStatus.OK);
        return response;
    }

    /**
     * Retrieves all recipe model objects.
     *
     * @return The model object that has been created detailing all recipes.
     */
    @Operation(description = "Returns all recipes and their associated unique ids that have been created.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully returned all recipes.")})
    @RequestMapping(value = EatsyRecipeEndpoints.RETRIEVE_ALL_RECIPES, method = {RequestMethod.GET})
    @ResponseBody
    public List<RecipeModel> retrieveAllRecipes() {

        logger.debug("A new request has been made to retrieve all recipes");
        return recipeFactoryHandler.retrieveAllRecipes();
    }

    /**
     * Deletes the requested recipe
     *
     * @param recipeKey the unique ID of the recipe object requested for deletion.
     * @return the updated list of all model recipe objects that has had the requested recipe removed.
     */
    @Operation(description = "Deletes the submitted recipe and returns the updated list of all recipes")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully deleted chosen recipe.")})
    @RequestMapping(value = EatsyRecipeEndpoints.DELETE_RECIPE, method = {RequestMethod.DELETE})
    @ResponseBody
    public List<RecipeModel> deleteRecipe(final String recipeKey) {

        logger.debug("A new request has been made to delete recipe: " + recipeKey);
        final List<RecipeModel> updatedRecipeList = recipeFactoryHandler.deleteRecipeAndReturnUpdatedRecipeList(recipeKey);
        return updatedRecipeList;

    }

    /**
     * Replaces the existing recipe with the updated version supplied in the PUT request.
     *
     * @param recipeModelWithUpdates the recipe model with the updated changes to be persisted.
     * @param recipeKey              the unique ID of the recipe. This will allow the recipe that needs to be
     *                               updated to be identified.
     * @return the updated recipeModel with the new updates/changes applied.
     */
    @Operation(description = "Replaces the existing recipe with the updated version supplied in the PUT request")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successfully updated chosen recipe.")})
    @RequestMapping(value = EatsyRecipeEndpoints.EDIT_RECIPE, method = {RequestMethod.PUT})
    @ResponseBody
    public RecipeModel editRecipe(
            @Parameter(description = "The recipe with the new information to update the existing recipe")
            @RequestBody final RecipeModel recipeModelWithUpdates, @PathVariable final String recipeKey) {

        logger.debug("A new request has been made to update recipe: " + recipeKey);
        final RecipeModel updatedRecipeModel = recipeFactoryHandler.updateRecipe(recipeKey, recipeModelWithUpdates);
        return updatedRecipeModel;
    }

    @GetMapping(path = {"/get/{imageKey}"})
    //@ResponseBody
    public RecipeImageEntity getImageWithMediaType(@PathVariable("imageKey") final String imageKey) throws IOException {
//        final InputStream in = getClass().getResourceAsStream("/images/image.jpg");
//        return IOUtils.toByteArray(in);
        return recipeFactoryHandler.retrieveRecipeImageEntity(imageKey);
    }

}
