package com.sneakershop.kiva.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//POJO - Plain Object Java Object
@Entity
@Table(name = "brands")
public class Brand {
	@Id
	@Column(name = "ID")
	private String ID;
	@Column(name = "Name")
	private String Name;
	@Column(name = "Image")
	private String Image;

	public Brand() {
	}

	public Brand(String iD, String name, String image) {
		super();
		ID = iD;
		Name = name;
		Image = image;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}
}
