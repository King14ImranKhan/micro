package com.appsdeveloperblog.photoapp.api.PhotoAppAPIConfigServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class PhotoAppAPIConfigServer {

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppAPIConfigServer.class, args);
	}

}
