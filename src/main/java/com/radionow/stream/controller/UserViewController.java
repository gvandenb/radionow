package com.radionow.stream.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radionow.stream.model.UserView;
import com.radionow.stream.model.ViewType;
import com.radionow.stream.service.UserService;
import com.radionow.stream.service.UserViewService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserViewController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserViewService userViewService;
	
	@PostMapping("/users/{uid}/views/episodes/{guid}")
	public ResponseEntity<UserView> createUserViewByObjectId(@PathVariable("uid") Long id, @PathVariable("guid") String guid, @RequestBody UserView userView) {
		try {
			
			UserView savedUserView = userViewService.save(userView);
			
			return new ResponseEntity<>(savedUserView, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/users/{uid}/views/stations/{guid}")
	public ResponseEntity<UserView> updateStationUserViewByObjectId(@PathVariable("uid") Long id, 
			@PathVariable("guid") String guid,
			@RequestParam(required = true) String time) {
		try {
			System.out.println("time1: " + (long)Double.parseDouble(time));
		
			UserView dataUserView = userViewService.findUserViewByUserIdAndObjectId(id, guid);
			if (dataUserView == null) {
				UserView userView = new UserView();
				System.out.println("time2: " + (long)Double.parseDouble(time));
				userView.setLastPlayedTime((long)Double.parseDouble(time));
				userView.setObjectId(guid);
				userView.setUserId(id);
				userView.setViewType(ViewType.STATION);
				dataUserView = userViewService.save(userView);
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			dataUserView.setViewType(ViewType.EPISODE);
			dataUserView.setLastPlayedTime((long)Double.parseDouble(time));
			
			UserView savedUserView = userViewService.save(dataUserView);
			return new ResponseEntity<>(savedUserView, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/users/{uid}/views/episodes/{guid}")
	public ResponseEntity<UserView> upadateUserViewByObjectId(@PathVariable("uid") Long id, 
															@PathVariable("guid") String guid,
															@RequestParam(required = true) String time) {
		try {
			System.out.println("time1: " + (long)Double.parseDouble(time));

			UserView dataUserView = userViewService.findUserViewByUserIdAndObjectId(id, guid);
			if (dataUserView == null) {
				UserView userView = new UserView();
				System.out.println("time2: " + (long)Double.parseDouble(time));
				userView.setLastPlayedTime((long)Double.parseDouble(time));
				userView.setObjectId(guid);
				userView.setUserId(id);
				userView.setViewType(ViewType.EPISODE);
				dataUserView = userViewService.save(userView);
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			dataUserView.setViewType(ViewType.EPISODE);
			dataUserView.setLastPlayedTime((long)Double.parseDouble(time));
			
			UserView savedUserView = userViewService.save(dataUserView);
			return new ResponseEntity<>(savedUserView, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{uid}/views/episodes/{guid}")
	public ResponseEntity<UserView> getUserViewByViewTypeAndObjectId(@PathVariable("uid") Long id, @PathVariable("guid") String guid) {
		try {
			UserView userView = userViewService.findUserViewByUserIdAndObjectId(id, guid);
			if (userView == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(userView, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/users/{uid}/views")
	public ResponseEntity<List<UserView>> getUserViewByUserId(@PathVariable("uid") Long id) {
		try {
			List<UserView> userViewList = userViewService.findUserViewByUserId(id);
			if (userViewList == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(userViewList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
