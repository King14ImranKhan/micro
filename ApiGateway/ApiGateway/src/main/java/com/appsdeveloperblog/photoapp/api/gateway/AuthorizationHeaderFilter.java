package com.appsdeveloperblog.photoapp.api.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import java.security.Key;
import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

	@Autowired
	Environment env;

	public AuthorizationHeaderFilter() {
		super(Config.class);
	}

	public static class Config {

	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
			}
			String authorizationHeader = request.getHeaders().get("Authorization").get(0);
			String jwt = authorizationHeader.replace("Bearer", "");
			if (!isJwtValid(jwt)) {
				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
			}
			return chain.filter(exchange);
		};
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private boolean isJwtValid(String jwt) {
	    boolean returnValue = true;
	    String subject = null;
	    try {
	        // Retrieve token secret from environment
	        String tokenSecret = env.getProperty("token.secret");

	        // Generate a secure key for HS512 using the retrieved token secret
	        Key secretKey = Keys.hmacShaKeyFor(tokenSecret.getBytes());

	        // Print the JWT for inspection
	        System.out.println("Received JWT: " + jwt);

	        // Attempt to parse the JWT using the secure key
	        subject = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwt).getBody().getSubject();
	    } catch (Exception ex) {
	        returnValue = false;
	        System.out.println("Exception during JWT parsing: " + ex.getMessage());
	        ex.printStackTrace(); // Print the full stack trace for detailed debugging
	    }

	    return returnValue;
	}
	
	

//	private boolean isJwtValid(String jwt) {
//		boolean returnValue = true;
//		String subject = null;
//		try {
//			subject = Jwts.parser().setSigningKey(env.getProperty("token.secret")).parseClaimsJws(jwt).getBody()
//					.getSubject();
//		} catch (Exception ex) {
//			returnValue = false;
//		}
//		if (subject == null || subject.isEmpty()) {
//			returnValue = false;
//		}
//
//		return returnValue;
//	}
}
