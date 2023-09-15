package com.rcs2.mcqsys;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class McqSysApplication {

	public static void main(String[] args) throws IOException, TimeoutException {
		SpringApplication.run(McqSysApplication.class, args);        
	}
}
