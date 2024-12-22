package be.api.services.impl;

import be.api.services.INotificationServices;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class NotificationServices implements INotificationServices {
    @Override
    public String sendNotification(String notificationMessage) {
        try {

            String registrationToken = "YOUR_REGISTRATION_TOKEN";

            Message message = Message.builder()
                    .putData("message", notificationMessage)
                    .setToken(registrationToken)
                    .build();

            String response = null;
            response = FirebaseMessaging.getInstance().send(message);
            System.out.println(response);
            return response;

        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
