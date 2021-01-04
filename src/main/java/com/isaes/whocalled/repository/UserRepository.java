package com.isaes.whocalled.repository;

import com.isaes.whocalled.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findByPhoneNo(String username);
}