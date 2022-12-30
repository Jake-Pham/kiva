package com.sneakershop.kiva.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sneakershop.kiva.entity.Comment;
import com.sneakershop.kiva.entity.Post;
import com.sneakershop.kiva.entity.Product;
import com.sneakershop.kiva.entity.User;
import com.sneakershop.kiva.exception.InternalServerException;
import com.sneakershop.kiva.model.request.CreateCommentPostRequest;
import com.sneakershop.kiva.model.request.CreateCommentProductRequest;
import com.sneakershop.kiva.repository.CommentRepository;
import com.sneakershop.kiva.service.CommentService;

import java.sql.Timestamp;

@Component
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment createCommentPost(CreateCommentPostRequest createCommentPostRequest, long userId) {
        Comment comment = new Comment();
        Post post = new Post();
        post.setId(createCommentPostRequest.getPostId());
        comment.setPost(post);
        User user = new User();
        user.setId(userId);
        comment.setUser(user);
        comment.setContent(createCommentPostRequest.getContent());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new InternalServerException("Có lỗi trong quá trình bình luận!");
        }
        return comment;
    }

    @Override
    public Comment createCommentProduct(CreateCommentProductRequest createCommentProductRequest, long userId) {
        Comment comment = new Comment();
        comment.setContent(createCommentProductRequest.getContent());
        comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        User user = new User();
        user.setId(userId);
        comment.setUser(user);
        Product product = new Product();
        product.setId(createCommentProductRequest.getProductId());
        comment.setProduct(product);
        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new InternalServerException("Có lỗi trong quá trình bình luận!");
        }
        return comment;
    }
}
