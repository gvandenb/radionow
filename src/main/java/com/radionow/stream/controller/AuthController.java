package com.radionow.stream.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.User;
//import com.twilio.rest.verify.v2.service.Verification;
//import com.twilio.rest.verify.v2.service.VerificationCheck;

/*
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
*/

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AuthController {

	
	//@Value("${twilio_account_sid}")
	private String ACCOUNT_SID;  // TODO: move to env var
	
	//@Value("${twilio_auth_token}")
	private String AUTH_TOKEN;		// TODO: move to env var
	
	//@Value("${twilio_path_service_sid}")
	private String PATH_SERVICE_SID;
	
	@GetMapping("/auth/send")
	public ResponseEntity<String> sendVerifyCode(@RequestParam(defaultValue = "0") String phoneNumber) {
		
		//System.out.println("ACCOUNT_SID: " + ACCOUNT_SID);
		//System.out.println("AUTH_TOKEN: " + AUTH_TOKEN);
		//System.out.println("PATH_SERVICE_SID: " + PATH_SERVICE_SID);
	    
		try {
			
		    //Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		    
		    /*
		    Verification verification = Verification.creator(
		    		PATH_SERVICE_SID,
		            "gvandenb@gmail.com",
		            "email")
		        .create();
		    Verification verification = Verification.creator(
		    		PATH_SERVICE_SID,
		    		"+14257502512",
		            "sms")
		        .create();
		    

		    System.out.println(verification.getSid());
			 		        */

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
			//Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
			/*
		    VerificationCheck verificationCheck = VerificationCheck.creator(
		    		PATH_SERVICE_SID, code)
		            .setTo("+14257502512")
		        .create();

		    System.out.println(verificationCheck.getSid());
		    System.out.println(verificationCheck.getStatus());
			 */
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			 return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}