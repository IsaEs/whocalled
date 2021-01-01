package com.isaes.whocalled.controller;

import com.isaes.whocalled.model.dto.JwtTokenModel;
import com.isaes.whocalled.model.dto.UserModel;
import com.isaes.whocalled.model.doa.User;
import com.isaes.whocalled.repository.UserRepository;
import com.isaes.whocalled.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.isaes.whocalled.config.security.SecurityConstants.EXPIRATION_TIME;
import static com.isaes.whocalled.config.security.SecurityConstants.SECRET;


@Slf4j
@RestController
@CrossOrigin
@RequestMapping(path="/api")
public class AuthenticationController {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private PasswordEncoder bcryptEncoder;
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody UserModel user) throws Exception {

		if(null == user.getPhoneNo() || null == user.getPassword()){
			return ResponseEntity.status(400).body("You need to define phone number and password");
		}
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(user.getPhoneNo());

		String token = Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME ))
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes((StandardCharsets.UTF_8)))
				.compact();
		return ResponseEntity.ok(new JwtTokenModel(token));
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserModel user){
		User newUser = new User();
		if(null ==user.getPhoneNo() || null == user.getPassword()){
			return ResponseEntity.status(400).body("You need to define phone number and password");
		}
		newUser.setPhoneNo(user.getPhoneNo());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		if (null != user.getNotification()) {
			newUser.setLanguage(user.getLanguage());
		} else {
			newUser.setLanguage("en");
		}
		newUser.setNotification(true);
		try {
			return ResponseEntity.ok(userRepository.save(newUser));
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(409).body("This user already registered.");
		}
	}

}