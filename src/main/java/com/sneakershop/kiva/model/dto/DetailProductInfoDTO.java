package com.sneakershop.kiva.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.sneakershop.kiva.entity.Comment;
import com.sneakershop.kiva.entity.Brand;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DetailProductInfoDTO {
	private String id;

    private String name;

    private String slug;

    private long price;

    private int views;

    private long totalSold;

    private ArrayList<String> productImages;

    private ArrayList<String> feedbackImages;

    private long promotionPrice;

    private String couponCode;

    private String description;

    private Brand brand;

    private List<Comment> comments;
}
