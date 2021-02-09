package com.example.udemy.course;

import com.example.udemy.course.services.FirstReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UdemyCourseApplication {

	@Autowired
	private FirstReport firstReport;

	@GetMapping("/exportTheReport}")
	public void exportTheReport() {
		firstReport.executeFirstReport();
	}

	public static void main(String[] args) {
		SpringApplication.run(UdemyCourseApplication.class, args);
	}

}
