package com.sneakershop.kiva.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sneakershop.kiva.models.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
