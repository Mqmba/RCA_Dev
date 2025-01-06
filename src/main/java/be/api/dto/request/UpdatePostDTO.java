package be.api.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePostDTO implements Serializable {
    int id;
    String title;
    String content;
}