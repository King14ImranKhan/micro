package com.appsdeveloperblog.photoapp.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import reactor.core.publisher.Mono;

// In place of writing PreFilter and PostFilter Class separately. We can have both in one
// class like this.
// Here in ordering prefilter will get executed like 1->2->3 but post filter will get executed
// 3->2->1
@Configuration
public class GlobalFiltersConfiguration {
	
	final Logger logger = LoggerFactory.getLogger(GlobalFiltersConfiguration.class);
	@Order(1)
	@Bean
	public GlobalFilter secondPreFilter() {
		return (exchange,chain) ->{
			
			logger.info("My First Pre Global filter is executed..");
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
			logger.info("My Third Post Global filter is executed..");
			}));
		};
		
	}
	@Order(2)
	@Bean
	public GlobalFilter thirdPreFilter() {
		return (exchange,chain) ->{
			
			logger.info("My Second Pre Global filter is executed..");
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				logger.info("My Second Post Global filter is executed..");
				}));
			};
		
	}
	
	@Order(3)
	@Bean
	public GlobalFilter fourthPreFilter() {
		return (exchange,chain) ->{
			
			logger.info("My Third Pre Global filter is executed..");
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				logger.info("My first Post Global filter is executed..");
				}));
			};
		
	}

}
