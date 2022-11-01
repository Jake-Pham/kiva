package com.sneakershop.kiva.mapping;

import org.springframework.stereotype.Component;

import com.sneakershop.kiva.models.entity.Brand;
import com.sneakershop.kiva.models.request.BrandRequest;

@Component
public class BrandMapping {

	public BrandRequest entityToDTO(Brand brand) {
		// TODO Auto-generated method stub
		BrandRequest dto = new BrandRequest();
		dto.setName(brand.getName());
		dto.setImage(brand.getImage());
		return dto;
	}


	public Brand dtoToEntiry(BrandRequest dto) {
		// TODO Auto-generated method stub
		Brand entity = new Brand();
		entity.setName(dto.getName());
		entity.setImage(dto.getImage());
		return entity;
	}
}
