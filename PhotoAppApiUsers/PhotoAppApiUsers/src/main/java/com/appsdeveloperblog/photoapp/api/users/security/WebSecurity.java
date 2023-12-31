package com.appsdeveloperblog.photoapp.api.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.appsdeveloperblog.photoapp.api.users.ui.service.UserService;

//import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
@Configuration
@EnableWebSecurity
public class WebSecurity {
	
	private Environment environment;
	private UserService usersService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public WebSecurity(Environment environment, UserService usersService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.environment = environment;
		this.usersService = usersService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
    
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    	
    	// Configure AuthenticationManagerBuilder
    	AuthenticationManagerBuilder authenticationManagerBuilder = 
    			http.getSharedObject(AuthenticationManagerBuilder.class);
    	
    	authenticationManagerBuilder.userDetailsService(usersService)
    	.passwordEncoder(bCryptPasswordEncoder);
    	
    	AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
    	
    	// Create AuthenticationFilter
   	AuthenticationFilter authenticationFilter = 
    			new AuthenticationFilter(usersService, environment, authenticationManager);
    	authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));
    	
    	http.csrf((csrf) -> csrf.disable());
  
    	http.authorizeHttpRequests((authz) -> authz
    	.requestMatchers(new AntPathRequestMatcher("/users", "POST")).permitAll()
    	.requestMatchers(new AntPathRequestMatcher("/users", "GET")).permitAll()
    	.requestMatchers(new AntPathRequestMatcher("/users/**", "GET")).permitAll()
    	.requestMatchers(new AntPathRequestMatcher("/actuator/**", "GET")).permitAll()
    	//.requestMatchers(new AntPathRequestMatcher("/login", "POST")).permitAll())
    	.and()
        .addFilter(authenticationFilter))
        .authenticationManager(authenticationManager)
        .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
 
    	http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.sameOrigin()));
        return http.build();

    }
}
