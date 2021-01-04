package com.isaes.whocalled.service;

import com.isaes.whocalled.model.Notification;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Data
@Slf4j
@Service
public class NotificationService implements INotificationService {
    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/queue/notifications";
    private final SimpMessagingTemplate simpMessagingTemplate;
    // Sake of simplicity I will keep open sessions in this hashmap. I understand this is not a proper implementation and
    // As I know its better to keep those in Redis or Memcached.
    HashMap<String, String> activeSessionMap = new HashMap<>(); // Key: PhoneNumber,  Value: PrincipalName
    // And again instead of using  bidiMap implementation I will keep two variable.
    // I am not sure i will need this.
    HashMap<String, String> inverseActiveSessionMap = new HashMap<>(); // Key: PrincipalName,  Value: PhoneNumber

    NotificationService(SimpMessagingTemplate simpMessagingTemplate) {
        log.info(simpMessagingTemplate.getUserDestinationPrefix());
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendNotificationToAll() {
        activeSessionMap.forEach((k,v)->{
            simpMessagingTemplate.convertAndSendToUser(v,WS_MESSAGE_TRANSFER_DESTINATION, "Notification from server " + new Date().toString());
        });
    }

    public void sendNotification(String randomPrincipalName,Object payload){
        simpMessagingTemplate.convertAndSendToUser(randomPrincipalName,WS_MESSAGE_TRANSFER_DESTINATION, payload);
    }

    public void sendNotification(String phoneNumber, Notification notification) {
        sendNotification(findOnlineUser(phoneNumber),(Object) notification);
    }

    public void sendNotificationWithPrincipleId(String principleId, Notification notification) {
        sendNotification(principleId,(Object) notification);
    }

    public void addToActiveSessions(String phone,String randomPrincipalName) {
        log.info("Phone: {} added to active sessions",phone );
        activeSessionMap.put(phone,randomPrincipalName);
        inverseActiveSessionMap.put(randomPrincipalName,phone);
    }

    public String findOnlineUser(String phoneNumber){
        return activeSessionMap.get(phoneNumber);
    }

    public String findOnlineUserByPrincipalName(String principalName){
      return inverseActiveSessionMap.get(principalName);
    }

}