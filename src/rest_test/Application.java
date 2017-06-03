package rest_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DavidCohen55
 * @author Shahar-Y
 * Created: May 2017
 * 
 * The main application, running in the background for the local host services.
 */

@SpringBootApplication
public class Application {
	ServerUser globalUser;
		
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
