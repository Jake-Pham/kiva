package com.sneakershop.kiva.controllers;

import com.sneakershop.kiva.models.entity.Brand;
import com.sneakershop.kiva.models.request.BrandRequest;
import com.sneakershop.kiva.service.BrandService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "brands")
//@RequestMapping(path = "/api/v1/brands")
//http:localhost:8080/brands
public class BrandController {
	@Autowired
	private BrandService brandService;
	// return name of "jsp file"

	@RequestMapping(value = "", method = RequestMethod.GET) 
	public String getAllCategories(ModelMap modelMap) { 
		List<Brand> brands = brandService.getAllBrands(); 
		modelMap.addAttribute("brands", brands); 
		return"user/brand"; 
		}

	/*
	 * @RequestMapping(value = "/add", method = RequestMethod.POST) public Brand
	 * addBrand(BrandRequest dto) { return brandService.addBrand(dto); }
	 */
}
