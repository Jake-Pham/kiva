package com.sneakershop.kiva.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "home")
public class HomeController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	 public String getAllCategories(ModelMap modelMap) {
	        //data sent to jsp => ModelMap
	        //send data from Controller to View
	        return "user/index";
	    }
}
