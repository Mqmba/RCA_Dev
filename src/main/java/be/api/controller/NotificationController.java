package be.api.controller;

import be.api.dto.response.ResponseData;
import be.api.services.impl.NotificationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServices notificationServices;
    @PostMapping("/send-notification")
    ResponseData<String> sendNotification(String message) {
        try{
            return new ResponseData<>(200,"Send success",notificationServices.sendNotification(message));
        }catch (Exception e) {
            return new ResponseData<>(500,"send fails",e.getMessage());
        }
    }
}
