package be.api.controller;

import be.api.dto.response.ResponseData;
import be.api.dto.response.ResponseError;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseData<?>uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            System.out.println(uploadResult);

            String imageUrl = (String) uploadResult.get("url");

            return new ResponseData<>(HttpStatus.OK.value(), "Upload thành công", imageUrl);
        }   catch (Exception e){
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
