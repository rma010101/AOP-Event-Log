package com.aop_example.event_log_aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aop_example.event_log_aop.model.Event;

@SpringBootApplication
public class EventLogAopApplication implements CommandLineRunner {

	@Autowired
	private Event event;

	public static void main(String[] args) {
		SpringApplication.run(EventLogAopApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=== AOP Demo Starting ===");
		System.out.println();
		
		// Call the Event.play() method - this will trigger all the AOP advice
		event.play();
		
		System.out.println();
		System.out.println("=== AOP Demo Complete ===");
	}
}
