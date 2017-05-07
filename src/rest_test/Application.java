package rest_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import main.java.data.members.User;


@SpringBootApplication
public class Application {
	
	User u;
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
