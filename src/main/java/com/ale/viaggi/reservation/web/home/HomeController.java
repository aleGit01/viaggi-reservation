package com.ale.viaggi.reservation.web.home;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@RequestMapping("/")
	public String index() {
		return "Greetings from Spring Boot! Microservice Reservation in progress :) ";
	}

}
