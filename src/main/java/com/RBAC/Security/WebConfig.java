package com.RBAC.Security;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer{
	 @Override
	    public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/404").setViewName("404");
	    }

	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// this path is correct and matches where the files are actually stored
	        registry.addResourceHandler("/uploads/**")
	                .addResourceLocations("file:C:/Users/AMAN/Downloads/Profile/");
	    }

}
