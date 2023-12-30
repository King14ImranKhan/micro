package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.photoapp.api.users.model.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersControllers {
	@Autowired
	UserService userService;
	
	@Autowired
	Environment env;
	
	@GetMapping(value="/status/check")
	public String getStatus() {
		
		return "Working on port "+env.getProperty("local.server.port")+", with token = "+env.getProperty("token.secret");
	}
	
	@PostMapping
	public ResponseEntity<CreateUserRequestModel> createUser(@RequestBody CreateUserRequestModel userDetails) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		CreateUserRequestModel returnValue = modelMapper.map(createdUser, CreateUserRequestModel.class);
		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
	}

}
