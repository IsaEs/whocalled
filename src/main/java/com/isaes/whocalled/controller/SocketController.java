package com.isaes.whocalled.controller;

import com.isaes.whocalled.model.dao.CallDetailRecord;
import com.isaes.whocalled.model.dao.User;
import com.isaes.whocalled.repository.CallDetailRepository;
import com.isaes.whocalled.repository.UserRepository;
import com.isaes.whocalled.service.INotificationService;
import com.isaes.whocalled.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.*;

@Slf4j
@RestController
public class SocketController {
    private final INotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CallDetailRepository callDetailRepository;

    SocketController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/register")
    public void registerNotification(Principal principal, @Header("phone")String phone) throws Exception {
        log.info("Phone Number of user: {}", phone);
        log.info(principal.getName());
        User user = userRepository.findByPhoneNo(phone);

        if (user.getNotification()){
            log.info("User: {}",user.getPhoneNo());
            notificationService.addToActiveSessions(phone,principal.getName());
            List<CallDetailRecord> missedCalls = callDetailRepository.findMissedCallsByUsername(user);
            log.info("Missed Call Size: {}",missedCalls.size());
            if(missedCalls.size()>0){
                Notification missedCallNotifications = new Notification.Builder()
                        .withLang(user.getLanguage())
                        .withMissedCalls(missedCalls)
                        .buildMissedCallNotifications();

                //UI will send rest request when its obtain Message  and will change to this records
                // Is dialed number notified field to true.
                //STEP ONE send missedCallNotifications
                notificationService.sendNotificationWithPrincipleId(principal.getName(),missedCallNotifications);
            }
            // STEP TWO
            Map<String,CallDetailRecord> onlineUsers = onlineUsers(missedCalls);
            onlineUsers.forEach((principleId,call)->{
                //In here we are sending notification to callers so we need dialed numbers.
                Notification availabilityNotifications = new Notification.Builder()
                        .withNumber(call.getDialedNumber())
                        .atTime(call.getLastCallTime())
                        .buildAvailabilityNotification();
                notificationService.sendNotificationWithPrincipleId(principleId,availabilityNotifications);
            });
        }
    }

    private Map<String,CallDetailRecord> onlineUsers(List<CallDetailRecord> missedCalls){
        HashMap<String,CallDetailRecord> onlineUsers = new HashMap<>();
        log.info("Missed Call {}", missedCalls.toString());
        missedCalls.sort(Comparator.comparing(CallDetailRecord::getLastCallTime));
        log.info("Sorted Call {}", missedCalls.toString());
        missedCalls.forEach(call-> {
            String principleId = notificationService.findOnlineUser(call.getCallerNo());
            if (null != principleId) onlineUsers.put(principleId,call);
        });
        return onlineUsers;
    }

    @MessageMapping("/call")
    public void callNumber(Principal principal, @Header("phone")String dialedNumber){
        String phoneNo = notificationService.findOnlineUserByPrincipalName(principal.getName());
        log.info("Dialed Number: {} Phone: {}", dialedNumber, phoneNo);
        log.info("Principal name {}", principal.getName());

        if(null!=phoneNo && phoneNo.equals(dialedNumber)){
            Notification notification = new Notification.Builder("You cannot call yourself.").build();
            notificationService.sendNotification(dialedNumber,notification);
            return;
        }
        User user = userRepository.findByPhoneNo(dialedNumber);
        if (null==user){
            Notification notification = new Notification.Builder("There is no registered user with this number!").build();
            notificationService.sendNotification(phoneNo,notification);
            return;
        }
        String principalName = notificationService.findOnlineUser(dialedNumber);
        if (null == principalName){
            Notification notification = new Notification.Builder("User is not available right now!").build();
            //TODO save call.
            notificationService.sendNotification(phoneNo,notification);
            return;
        }

        Notification notification = new Notification.Builder("You are getting call from "+phoneNo).build();
        notificationService.sendNotificationWithPrincipleId(principalName,notification);
    }
}
