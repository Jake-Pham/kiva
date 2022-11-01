package com.sneakershop.kiva.service;


import java.util.List;

import com.sneakershop.kiva.models.entity.Brand;
import com.sneakershop.kiva.models.request.BrandRequest;

public interface BrandService {
	List<Brand> getAllBrands();
	Brand addBrand(BrandRequest dto);
	Brand updateBrand(BrandRequest dto, int id);
	public void deleteBrandById(int id);
	Brand findById(int id);
}
