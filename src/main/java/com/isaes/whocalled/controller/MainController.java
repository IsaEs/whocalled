package com.isaes.whocalled.controller;

import com.isaes.whocalled.model.doa.CallDetailRecord;
import com.isaes.whocalled.model.doa.User;
import com.isaes.whocalled.model.dto.RingModel;
import com.isaes.whocalled.repository.CallDetailRepository;
import com.isaes.whocalled.repository.UserRepository;
import com.isaes.whocalled.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path="/api")
public class MainController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CallDetailRepository callDetailRepository;
    private final NotificationService notificationService;
    MainController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String helloWorld(){
        notificationService.sendNotificationToAll();
        return "Hello World";
    }

    @GetMapping("/user")
    public String user(Principal principal){

        User user = userRepository.findByPhoneNo(principal.getName());
        log.info("User: {}",user.getPhoneNo());
        List<CallDetailRecord> missedCalls = callDetailRepository.findCallDetailRecordsByUsername(user);

        if (null!=missedCalls){
            log.info("Missed Calls: {}",missedCalls.toString());
        }else {
            log.info("Missed Calls is null");
        }
        return "Hello User";
    }

    @GetMapping("/admin")
    public String admin(){
        return "Hello Admin";
    }

}