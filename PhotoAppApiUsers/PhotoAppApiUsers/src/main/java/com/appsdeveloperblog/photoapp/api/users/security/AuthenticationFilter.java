package com.appsdeveloperblog.photoapp.api.users.security;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appsdeveloperblog.photoapp.api.users.model.LoginRequestModel;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private UserService usersService;
	private Environment environment;
	
	public AuthenticationFilter(UserService usersService,
			Environment environment,
			AuthenticationManager authenticationManager) {
		super(authenticationManager);
		this.usersService = usersService;
		this.environment=environment;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,HttpServletResponse res) throws AuthenticationException{
		try {
			LoginRequestModel creds = new ObjectMapper()
					.readValue(req.getInputStream(),LoginRequestModel.class);
			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(creds.getEmail(),creds.getPassword()
					,new ArrayList<>()));
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
	        FilterChain chain, Authentication auth) throws IOException, ServletException {
	    String userName = ((User) auth.getPrincipal()).getUsername();
	    UserDto userDetails = usersService.getUserDetailsByEmail(userName);

	    // Retrieve token secret from environment
	    String tokenSecret = environment.getProperty("token.secret");

	    // Generate a secure key for HS512 using the retrieved token secret
	    Key secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes());

	    Instant now = Instant.now();

	    // Build the JWT
	    String token = Jwts.builder()
	            .setSubject(userDetails.getUserId())
	            .setExpiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
	            .setIssuedAt(Date.from(now))
	            .signWith(secretKey, SignatureAlgorithm.HS512)
	            .compact();

	    // Add JWT and user-related information to response headers
	    res.addHeader("token", token);
	    res.addHeader("userId", userDetails.getUserId());
	}
//	@Override
//	protected void successfulAuthentication(HttpServletRequest req,HttpServletResponse res,
//		FilterChain chain, Authentication auth)throws IOException,ServletException {
//		String userName = ((User)auth.getPrincipal()).getUsername();
//		UserDto userDetails = usersService.getUserDetailsByEmail(userName);
//		String tokenSecret = environment.getProperty("token.secret");
//		//byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
//		//SecretKeySpec secretKey = new SecretKeySpec(secretKeyBytes,SignatureAlgorithm.HS512.getJcaName());
//		Key  secretKey= Keys.secretKeyFor(SignatureAlgorithm.HS512);
//		Instant now = Instant.now();
//		String token = Jwts.builder().setSubject(userDetails.getUserId())
//		.setExpiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
//		.setIssuedAt(Date.from(now))
//		.signWith(secretKey,SignatureAlgorithm.HS512)
//		.compact();
//		res.addHeader("token", token);
//		res.addHeader("userId", userDetails.getUserId());
//	}

}
