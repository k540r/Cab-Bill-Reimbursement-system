package com.RBAC.Security;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

public class CustomErrorController implements ErrorController{
	 @RequestMapping("/error")
	    public String handleError() {
	        return "error"; // Returns the error.html template
	    }

	    public String getErrorPath() {
	        return "/error";
	    }

}
