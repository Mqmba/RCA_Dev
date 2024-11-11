package be.api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {


    //    @Override
//    public void addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*");
//    }
//hello
    @PostConstruct
    public void initializeFirebase() {
        //replace the file inside data with your firebase config
        try (FileInputStream serviceAccount = new FileInputStream("Data/Firebase-serviceAccountKey.json")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
