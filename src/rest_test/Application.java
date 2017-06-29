package rest_test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;


/**
 * @author DavidCohen55
 * @author Shahar-Y Created: May 2017
 * 
 *         The main application, running in the background for the local host
 *         services.
 */

@SpringBootApplication
public class Application {
	ServerUser globalUser;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
