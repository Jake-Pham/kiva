package com.sneakershop.kiva.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sneakershop.kiva.models.Brand;

public interface BrandRepository extends CrudRepository<Brand, String> {
}
