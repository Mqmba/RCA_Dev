package be.api.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "hotrohoctap");
        config.put("api_key", "398533167669176");
        config.put("api_secret", "2P32JSJ8P9PTnSaJ55WujgRiqWU");

        return new Cloudinary(config);
    }


}
