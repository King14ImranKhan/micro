package com.appsdeveloperblog.photoapp.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
// This class is for Post-Filter
public class MyPostFilter implements GlobalFilter , Ordered{

	final Logger logger = LoggerFactory.getLogger(MyPostFilter.class);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			logger.info("My Last Post-Filter is executed..");
		}));
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}


//public class MyPostFilter implements GlobalFilter{
//
//	final Logger logger = LoggerFactory.getLogger(MyPostFilter.class);
//	
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		
//		return chain.filter(exchange).then(Mono.fromRunnable(()->{
//			logger.info("My First Post-Filter is executed..");
//		}));
//	}
//}
