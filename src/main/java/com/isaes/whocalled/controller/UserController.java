package com.isaes.whocalled.controller;

import com.isaes.whocalled.model.dao.CallDetailRecord;

import com.isaes.whocalled.model.dao.User;
import com.isaes.whocalled.model.dto.PreferenceModel;
import com.isaes.whocalled.model.dto.RingModel;
import com.isaes.whocalled.repository.CallDetailRepository;
import com.isaes.whocalled.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(path="/api/user/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CallDetailRepository callDetailRepository;

    @PostMapping("/ring")
    public ResponseEntity<?> ringNumber(Principal principal,@RequestBody RingModel requestBody) throws Exception {
        log.info("Number {} requested for call : {}",principal.getName(),requestBody.toString());
        CallDetailRecord callDetailRecord = new CallDetailRecord();
        User user = userRepository.findByPhoneNo(requestBody.getDialedNumber());
        if(null!=requestBody.getDialedNumber() && null != user){
            //TODO field validations
            callDetailRecord.setCallerNo(principal.getName());
            callDetailRecord.setDialedNumber(requestBody.getDialedNumber());
            callDetailRecord.setNumberOfRings(requestBody.getNumberOfRings());
            callDetailRecord.setLastCallTime(requestBody.getLastCallTime());
            callDetailRecord.setIsDialedNumberNotified(false);
            return ResponseEntity.ok(callDetailRepository.save(callDetailRecord));
        }
        return ResponseEntity.status(409).body("There is no such user.");
    }

    @PutMapping("/phone")
    public ResponseEntity<?> updateUserPreferences(Principal principal, @RequestBody PreferenceModel requestBody)  {
        log.info(requestBody.toString());
        log.info(principal.getName());
        User user = userRepository.findByPhoneNo(principal.getName());
        user.setLanguage(requestBody.getLanguage());
        user.setNotification(requestBody.getNotification());
        try {
            userRepository.save(user);
            return ResponseEntity.ok("Your preferences updated.");
        } catch (DataIntegrityViolationException e){
            log.error("Unique key constraint error");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("We couldn't operate this request right now.Please try again later.");
        }
    }

    @PutMapping("/notification")
    public ResponseEntity<?> registerNotification(Principal principal, @RequestBody PreferenceModel requestBody)  {
        //TODO mark missed notifications as notified.
        return ResponseEntity.ok("Your preferences updated.");
    }

}
