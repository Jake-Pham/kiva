package com.sneakershop.kiva.models.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="price")
	private  BigDecimal price;
	@Column(name="amount")
	private int amount;
	@Column(name="status")
	private int status;
	@Column(name="created_date")
	private LocalDate created_date;
	@Column(name="brand_id")
	private int brand_id;
	@Column(name="type_id")
	private int type_id;
	@Column(name="discount_id")
	private int discount_id;
	public Product() {
		super();
	}
	public Product(int id, String name, BigDecimal price, int amount, int status, LocalDate created_date, int brand_id,
			int type_id, int discount_id) {
		super();
		id = id;
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.status = status;
		this.created_date = created_date;
		this.brand_id = brand_id;
		this.type_id = type_id;
		this.discount_id = discount_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public LocalDate getCreated_date() {
		return created_date;
	}
	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}
	public int getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(int brand_id) {
		this.brand_id = brand_id;
	}
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public int getDiscount_id() {
		return discount_id;
	}
	public void setDiscount_id(int discount_id) {
		this.discount_id = discount_id;
	}
	
	
	
}
