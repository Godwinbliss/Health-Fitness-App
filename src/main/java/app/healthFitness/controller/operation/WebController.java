package app.healthFitness.controller.operation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * Maps the client's GET requests to a html page.
 */
@Controller
public class WebController {

	@GetMapping( "")
	public String viewHomePage(){
		return "public/index.html";
	}

	@GetMapping("/home")
	public String getHomePage(){
		return "user/home.html";
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return "public/login.html";
	}

	@GetMapping("/register")
	public String getRegisterPage() {
		return "public/register.html";
	}

}