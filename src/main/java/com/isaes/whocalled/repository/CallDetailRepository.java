package com.isaes.whocalled.repository;

import com.isaes.whocalled.model.dao.CallDetailRecord;

import com.isaes.whocalled.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallDetailRepository extends JpaRepository<CallDetailRecord, Integer> {

    @Query("select u from CallDetailRecord u where u.dialedNumber = :#{#user.phoneNo} and u.isDialedNumberNotified = false")
    List<CallDetailRecord> findMissedCallsByUsername(@Param("user") User user);

}