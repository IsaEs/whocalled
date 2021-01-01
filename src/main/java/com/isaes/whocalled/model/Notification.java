package com.isaes.whocalled.model;

import com.isaes.whocalled.model.doa.CallDetailRecord;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 Missed calls:
     534333885 Nov 11 at 12:11 (3)
     534333885 Nov 11 at 12:10 (1)

 Sizi Arayan Numaralar:
     534333885 Kasim 11 saat 12:31 (1)
     534333885 Kasim 11 saat 12:31 (1)

 The number you have been called 534444455 at Nov 12 10:22 is available now.
 12 Kasim 10:22'de aradiginiz
 */
@Getter
@Setter
@Slf4j
public class Notification {

    private String message;
    private String language;
    private List<Long> notificationIds;

    private Notification(){}

    public static class Builder{
        private String message = null;
        private String atTime;
        private String msisdn;
        private String lang = "en";
        private List<CallDetailRecord> missedCalls;

        public Builder(){}
        public Builder(String message){
            this.message= message;
        }

        public Builder atTime(String atTime){
            this.atTime = atTime;
            return this;
        }

        public Builder withLang(String lang){
            this.lang = lang;
            return this;
        }

        public Builder withNumber(String number){
            this.msisdn = number;
            return this;
        }


        public Builder withMissedCalls(List<CallDetailRecord> missedCalls){
            this.missedCalls = missedCalls;
            return this;
        }

        public Notification buildAvailabilityNotification(){
            Notification notification = new Notification();
            String message = String.format("The number you have been called %s at %s is available now.",msisdn,atTime);
            notification.setMessage(message);
            notification.setLanguage(lang);
            return notification;
        }


        public Notification buildMissedCallNotifications(){
            Notification notification = new Notification();
            StringBuilder missedOnes = new StringBuilder("You have a missed calls: ");
             List<Long> notificationIds = new ArrayList<>();
            missedCalls.forEach(call->{
                notificationIds.add(call.getId());
                missedOnes.append(String.format("%s call at %s - (%s) \n",call.getCallerNo(),call.getLastCallTime(),call.getNumberOfRings()));
            });
            log.info(missedOnes.toString());
            notification.setMessage(missedOnes.toString());
            notification.setLanguage(lang);
            notification.setNotificationIds(notificationIds);
            return notification;
        }

        public Notification build(){
            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setLanguage(lang);
            return notification;
        }
    }

}
