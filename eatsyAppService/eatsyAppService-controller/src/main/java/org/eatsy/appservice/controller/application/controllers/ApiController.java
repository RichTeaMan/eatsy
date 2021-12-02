package org.eatsy.appservice.controller.application.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.service.RecipeFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * API Controller. All handling methods on this controller are relative to the /api path.
 */
@RestController
@RequestMapping("/api")
@EnableAutoConfiguration
public class ApiController {

    //logger
    private static final Logger logger = LogManager.getLogger(ApiController.class);

    //Interface for recipe operations
    private RecipeFactory recipeFactoryHandler;

    //Inject the dependency of the recipeFactory implementation into the api controller during instantiation.
    public ApiController(RecipeFactory recipeFactoryHandler) {
        this.recipeFactoryHandler = recipeFactoryHandler;
    }


    /**
     * "Returns a new recipe with the information provided in the request"
     *
     * @param recipeModel The recipe the user is adding.
     * @return the recipe model object that has been created.
     */
    @ApiOperation("Returns a new recipe with the information provided in the request")
    @ApiResponses({@ApiResponse(code = 200, message = "Successfully created new recipe.")})
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseEntity<RecipeModel> addRecipe(
            @ApiParam("The recipe to be created.") @RequestBody final RecipeModel recipeModel) {

        logger.debug("A new request has been made to create a recipe called " + recipeModel.getName());
        RecipeModel newRecipeModel = recipeFactoryHandler.createRecipe(recipeModel);

        final ResponseEntity<RecipeModel> response = new ResponseEntity<RecipeModel>(newRecipeModel, HttpStatus.OK);
        return response;
    }


}
