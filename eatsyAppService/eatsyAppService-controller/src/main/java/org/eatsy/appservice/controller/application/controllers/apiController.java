package org.eatsy.appservice.controller.application.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.eatsy.appservice.domain.Recipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Controller. All handling methods on this controller are relative to the /api path.
 */
@RestController
@RequestMapping("/api")
@EnableAutoConfiguration
public class apiController {

    private static final Logger logger = LoggerFactory.getLogger(apiController.class);


    /**
     * TODO This is a test method to validate the spring boot service runs.
     *
     * @param recipeName The name of the recipe the user is adding.
     * @return the recipe object that has been created.
     */
    @ApiOperation("Returns a new recipe with the information provided in the request")
    @ApiResponses({ @ApiResponse(code = 200, message = "Successfully created new recipe.") })
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Recipe addRecipe(@ApiParam("The recipe name.") final String recipeName) {

        logger.debug("A new recipe has been created called " + recipeName);

        Recipe newRecipe = new Recipe(recipeName);
        return newRecipe;
    }


}
