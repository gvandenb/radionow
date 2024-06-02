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

	
	public static final String ACCOUNT_SID = "ACa437bddda03231c80f4c463dc51513bb";  // TODO: move to env var
	public static final String AUTH_TOKEN = "db2b3771fb695775eb38d8e847d71915";		// TODO: move to env var
	
	@GetMapping("/auth/send")
	public ResponseEntity<String> sendVerifyCode(@RequestParam(defaultValue = "0") String phoneNumber) {
		
		try {
			
		    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		    /*
		    Verification verification = Verification.creator(
		            "VA3326d2d0b4919afc0f53ebb9268593ee",
		            "+14257502512",
		            "sms")
		        .create();
		        */
		    Verification verification = Verification.creator(
		            "VA3326d2d0b4919afc0f53ebb9268593ee",
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
		            "VA3326d2d0b4919afc0f53ebb9268593ee", code)
		            .setTo("+14257502512")
		        .create();

		    System.out.println(verificationCheck.getSid());
		    System.out.println(verificationCheck.getStatus());
			 /*
			  * {
				  "status": "approved",
				  "payee": null,
				  "date_updated": "2024-05-03T21:17:02Z",
				  "account_sid": "ACa437bddda03231c80f4c463dc51513bb",
				  "to": "+14257502512",
				  "amount": null,
				  "valid": true,
				  "sid": "VE96098d8da9b5c5c952443dd26e57c4fc",
				  "date_created": "2024-05-03T21:14:45Z",
				  "service_sid": "VA3326d2d0b4919afc0f53ebb9268593ee",
				  "channel": "sms"
				}
			  */
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}