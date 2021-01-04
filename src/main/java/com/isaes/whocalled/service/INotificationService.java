package com.isaes.whocalled.service;

import com.isaes.whocalled.model.Notification;

public interface INotificationService {
    void sendNotificationToAll();

    void sendNotification(String randomPrincipalName,Object payload);

    void sendNotification(String phoneNumber, Notification notification);

    void sendNotificationWithPrincipleId(String principleId, Notification notification);

    void addToActiveSessions(String phone,String randomPrincipalName);

    String findOnlineUser(String phoneNumber);

    String findOnlineUserByPrincipalName(String principalName);
}
