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
        // This registration token comes from the client FCM SDKs.
        try {

            String registrationToken = "YOUR_REGISTRATION_TOKEN";

            // See documentation on defining a message payload.
            Message message = Message.builder()
                    .putData("score", "850")
                    .putData("time", "2:45")
                    .setToken(registrationToken)
                    .build();

            // Send a message to the device corresponding to the provided
            // registration token.
            String response = null;
            response = FirebaseMessaging.getInstance().send(message);
            System.out.println(response);
            return response;

        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
