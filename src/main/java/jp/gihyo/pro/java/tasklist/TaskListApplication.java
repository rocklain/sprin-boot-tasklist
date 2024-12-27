package jp.gihyo.pro.java.tasklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@SpringBootApplication
public class TaskListApplication {

	public static void main(String[] args) {

		SpringApplication.run(TaskListApplication.class, args);
	}

}

