package be.api.controller;

import be.api.dto.request.CreatePostDTO;
import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import be.api.services.impl.PostServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostServices postServices;

    @PostMapping("/create-post")
    public ResponseData<?> createPost(@RequestBody CreatePostDTO dto) {
        try {
            return new ResponseData<>(200, "Thành công", postServices.createPost(dto));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/get-all-post")
    public ResponseData<?> getAllPost() {
        try {
            return new ResponseData<>(200, "Thành công", postServices.getAllPost());
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/delete-post")
    public ResponseData<?> deletePost(@RequestParam int id) {
        try {
            postServices.deletePost(id);
            return new ResponseData<>(200, "Thành công", null);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
