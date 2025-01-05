package be.api.dto.request;


import lombok.Data;

import java.io.Serializable;

@Data
public class CreatePostDTO implements Serializable {
    String title;
    String content;
}
