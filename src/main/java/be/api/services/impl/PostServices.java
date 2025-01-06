package be.api.services.impl;

import be.api.dto.request.CreatePostDTO;
import be.api.dto.request.UpdatePostDTO;
import be.api.exception.BadRequestException;
import be.api.model.Post;
import be.api.model.User;
import be.api.repository.IPostRepository;
import be.api.repository.IUserRepository;
import be.api.services.IPostServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServices implements IPostServices {
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;

    @Override
    public int createPost(CreatePostDTO dto) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(userName);
        User admin = userRepository.findById(1).orElse(null);


        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImage(dto.getImage());
        post.setStatus(1);
        post.setUser(admin);
        postRepository.save(post);
        return post.getId();
    }

    @Override
    public boolean deletePost(int id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new BadRequestException("Không tìm thấy bài viết");
        }
        postRepository.delete(post);
        return true;
    }

    @Override
    public List<Post> getAllPost() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(int id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updatePost(UpdatePostDTO dto) {
        Post post = postRepository.findById(dto.getId()).orElse(null);
        if (post == null) {
            throw new BadRequestException("Không tìm thấy bài viết");
        }
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        postRepository.save(post);
        return true;
    }
}
