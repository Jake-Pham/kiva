package com.sneakershop.kiva.service;

import org.springframework.stereotype.Service;

import com.sneakershop.kiva.entity.Image;

import java.util.List;

@Service
public interface ImageService {
    void saveImage(Image image);
    void deleteImage(String uploadDir,String filename);
    List<String> getListImageOfUser(long userId);
}
