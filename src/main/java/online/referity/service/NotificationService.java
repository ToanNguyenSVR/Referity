package online.referity.service;

import online.referity.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    List<Notification> getByAccount ( UUID accountId);

    void sendNotification (String title , String body , String forwardUrl , UUID accountId);

    boolean  changeStatus ( UUID accountId );



}
