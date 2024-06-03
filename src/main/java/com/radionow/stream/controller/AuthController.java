package com.radionow.stream.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.User;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AuthController {

	
	public static final String ACCOUNT_SID = "";  // TODO: move to env var
	public static final String AUTH_TOKEN = "";		// TODO: move to env var
	public static final String PATH_SERVICE_SID = "";
	
	@GetMapping("/auth/send")
	public ResponseEntity<String> sendVerifyCode(@RequestParam(defaultValue = "0") String phoneNumber) {
		
		try {
			
		    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		    
		    Verification verification = Verification.creator(
		    		PATH_SERVICE_SID,
		            "gvandenb@gmail.com",
		            "email")
		        .create();

		    System.out.println(verification.getSid());
			 
			return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/auth/verify")
	public ResponseEntity<User> authVerifyCode(@RequestParam(defaultValue = "0") String code) {
		
		try {
			User user = new User();
			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		    VerificationCheck verificationCheck = VerificationCheck.creator(
		    		PATH_SERVICE_SID, code)
		            .setTo("+14257502512")
		        .create();

		    System.out.println(verificationCheck.getSid());
		    System.out.println(verificationCheck.getStatus());
			 
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}