package com.exam_organizer;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExamOrganizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamOrganizerApplication.class, args);
		System.out.println("started...");

	}

	@Bean
	public ModelMapper modelMapper(){
		return  new ModelMapper();
	}


}
