package com.aop_example.event_log_aop.model;

import org.springframework.stereotype.Component;

/**
 * Simple Event class for AOP demonstration
 */
@Component
public class Event {
    
    /**
     * Simple method that prints a message - perfect for AOP interception
     */
    public void play() {
        System.out.println("I am playing");
    }
}
