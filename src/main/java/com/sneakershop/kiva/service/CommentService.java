package com.sneakershop.kiva.service;

import org.springframework.stereotype.Service;

import com.sneakershop.kiva.entity.Comment;
import com.sneakershop.kiva.model.request.CreateCommentPostRequest;
import com.sneakershop.kiva.model.request.CreateCommentProductRequest;

@Service
public interface CommentService {
    Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest,long userId);
    Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, long userId);
}
