package org.eatsy.appservice.controller.application.controllers;

import io.swagger.annotations.ApiParam;
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
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Recipe addRecipe(final String recipeName) {
//        logger.trace("A TRACE Message");
//        logger.debug("A DEBUG Message");
        logger.info("An INFO Message. newRecipe " + recipeName);
//        logger.warn("A WARN Message");
//        logger.error("An ERROR Message");
        Recipe newRecipe = new Recipe(recipeName);
        return newRecipe;
    }


}
