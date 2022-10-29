package com.sneakershop.kiva.controllers;

import com.sneakershop.kiva.models.Brand;
import com.sneakershop.kiva.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "brands")
//http:localhost:8080/brands
public class BrandController {
    @Autowired //Inject "categoryRepository" - Dependency Injection
    private BrandRepository brandRepository;
    //return name of "jsp file"
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAllCategories(ModelMap modelMap) {
        //data sent to jsp => ModelMap
        //send data from Controller to View
        //modelMap.addAttribute("name", "Hoang");
        //modelMap.addAttribute("age", 18);
        Iterable<Brand> brands = brandRepository.findAll();
        modelMap.addAttribute("brands", brands);
        return "user/brand";
    }
}
