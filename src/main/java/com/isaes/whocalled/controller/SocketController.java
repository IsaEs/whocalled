package com.isaes.whocalled.controller;

import com.isaes.whocalled.model.doa.CallDetailRecord;
import com.isaes.whocalled.model.doa.User;
import com.isaes.whocalled.repository.CallDetailRepository;
import com.isaes.whocalled.repository.UserRepository;
import com.isaes.whocalled.service.NotificationService;
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
    private final NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CallDetailRepository callDetailRepository;

    SocketController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/registerOriginal")
    public void registerNotifications(Principal principal, @Header("phone")String phone, @Header("simpSessionId") String sessionId) throws Exception {
        log.info("Session id {}",sessionId);
        log.info("Phone Number: {}", phone); // This could be any other token or property to register user.
        log.info(principal.getName());
        notificationService.addToActiveSessions(phone,principal.getName());
        notificationService.sendNotification(principal.getName(),"You have been registered " + new Date().toString());
    }


    @MessageMapping("/register")
    public void registerNotification(Principal principal, @Header("phone")String phone, @Header("simpSessionId") String sessionId) throws Exception {
        log.info("Session id {}",sessionId);
        log.info("Phone Number: {}", phone); // This could be any other token or property to register user.
        log.info(principal.getName());
        User user = userRepository.findByPhoneNo(phone);

        if (user.getNotification()){
            log.info("User: {}",user.getPhoneNo());
            notificationService.addToActiveSessions(phone,principal.getName());
            List<CallDetailRecord> missedCalls = callDetailRepository.findCallDetailRecordsByUsername(user);
            log.info("Missed Call Size: {}",missedCalls.size());
            Notification missedCallNotifications = new Notification.Builder()
                    .withLang(user.getLanguage())
                    .withMissedCalls(missedCalls)
                    .buildMissedCallNotifications();

            //UI will send rest request when its obtain Message  and will change to this records
            // Is dialed number notified field to true.
            //STEP ONE send missedCallNotifications
            notificationService.sendNotificationWithPrincipleId(principal.getName(),missedCallNotifications);

            // STEP TWO
            missedCalls.forEach(call->{
                String principleId = notificationService.findActiveSession(call.getCallerNo());
                //
                if (principleId.equals("")){
                   //TODO save backend for later notification.
                }else {
                    //In here we are sending notification to callers so we need dialed numbers.
                    Notification availabilityNotifications = new Notification.Builder()
                            .withNumber(call.getDialedNumber())
                            .atTime("now")
                            .buildAvailabilityNotification();
                    notificationService.sendNotificationWithPrincipleId(principleId,availabilityNotifications);
                }
            });

        }
    }


//    /***
//     * We are looking for and online users to immediately notify it and we will create record for
//     * the other user who is not notified yet.
//     * Key= online for online users
//     * Key= offline for offline users
//     * @param missedCalls users missed calls from multiple callers
//     * @return List of active users principal ID
//     */
//    private Map<String, List<String>> getOnlineUsers(List<CallDetailRecord> missedCalls){
//        List<String> onlineUsersPrincipleIds = new ArrayList<>();
//        List<String> offlineUsersNames = new ArrayList<>();
//        missedCalls.forEach(call->{
//            String principleId = notificationService.findActiveSession(call.getCallerNo());
//            if (principleId.equals("")){
//                offlineUsersNames.add(call.getCallerNo());
//            }else {
//                onlineUsersPrincipleIds.add(principleId);
//            }
//
//        });
//        HashMap<String, List<String>> retVal = new HashMap<>();
//        retVal.put("online",onlineUsersPrincipleIds);
//        retVal.put("offline",offlineUsersNames);
//        return retVal;
//    }


    @MessageMapping("/call")
    public void callNumber(Principal principal, @Header("phone")String dialedNumber){

        String phoneNo = notificationService.findActiveSessionByPrincipleName(principal.getName());
        log.info("Dialed Number: {} Phone: {}", dialedNumber, phoneNo);
        log.info("Principal name {}", principal.getName());

        if(phoneNo.equals(dialedNumber)){
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
        String principalName = notificationService.findActiveSession(dialedNumber);
        if (principalName.equals("")){
            Notification notification = new Notification.Builder("User is not available right now!").build();
            //TODO save call.
            notificationService.sendNotification(phoneNo,notification);
            return;
        }

        Notification notification = new Notification.Builder("You are getting call from "+phoneNo).build();
        notificationService.sendNotificationWithPrincipleId(principalName,notification);
    }
}
