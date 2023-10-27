package online.referity.service.imp;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import online.referity.dto.FcmNotification;
import online.referity.exception.exceptions.BadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FcmService {
    private final FirebaseMessaging firebaseMessaging;
    private final FirebaseApp firebaseApp;

    public FcmService(FirebaseApp firebaseApp) {
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
        this.firebaseApp = firebaseApp;
    }

    public void sendPushNotification(FcmNotification fcmNotification) throws FirebaseMessagingException, FirebaseAuthException {
        System.out.println(fcmNotification.getToken());
        Notification notification = Notification.builder()
                .setTitle(fcmNotification.getTitle())
                .setBody(fcmNotification.getBody())
                .build();
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(fcmNotification.getToken())
                .build();

        try{
            firebaseMessaging.send(message);
        }catch (Exception e){
            e.printStackTrace();
            throw new BadRequest(e.getMessage());
        }
    }


}
