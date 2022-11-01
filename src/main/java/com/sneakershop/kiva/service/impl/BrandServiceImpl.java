package com.sneakershop.kiva.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sneakershop.kiva.mapping.BrandMapping;
import com.sneakershop.kiva.models.entity.Brand;
import com.sneakershop.kiva.models.request.BrandRequest;
import com.sneakershop.kiva.repositories.BrandRepository;
import com.sneakershop.kiva.service.BrandService;
@Service
public class BrandServiceImpl implements BrandService{
	@Autowired
	BrandRepository brandRepository;
	@Autowired
	BrandMapping brandMapping;

	@Override
	public List<Brand> getAllBrands() {
		// TODO Auto-generated method stub
		return brandRepository.findAll();
	}
	@Override
	public Brand addBrand(BrandRequest dto) {
		// TODO Auto-generated method stub
		Brand brand = new Brand();
		brand = brandMapping.dtoToEntiry(dto);
		return brandRepository.save(brand);
	}

	@Override
	public Brand updateBrand(BrandRequest dto, int id) {
		// TODO Auto-generated method stub
		Brand brand = brandRepository.findById(id).get();
		if (brand != null) {
			brand.setName(dto.getName());
			brand.setImage(dto.getImage());
			return brandRepository.save(brand);
		} else {
			return null;
		}
	}

	@Override
	public void deleteBrandById(int id) {
		brandRepository.deleteById(id);

	}

	@Override
	public Brand findById(int id) {
		// TODO Auto-generated method stub
		return brandRepository.findById(id).get();
	}

}
