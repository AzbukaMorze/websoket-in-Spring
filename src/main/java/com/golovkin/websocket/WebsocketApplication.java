package com.golovkin.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class WebsocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}

}
