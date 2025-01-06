package be.api.services;

import be.api.dto.request.CreatePostDTO;
import be.api.dto.request.UpdatePostDTO;
import be.api.model.Post;

import java.util.List;

public interface IPostServices {
    int createPost(CreatePostDTO dto);
    boolean deletePost(int id);
    List<Post> getAllPost();
    Post getPostById(int id);
    boolean updatePost (UpdatePostDTO dto);

}

