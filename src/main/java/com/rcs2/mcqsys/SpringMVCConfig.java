package com.rcs2.mcqsys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class SpringMVCConfig extends WebMvcConfigurerAdapter {
	
	@Value("${file.image.location}") 
	private String imageLocation;
	
	@Value("${file.static.location}") 
	private String staticLocation;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**","questions/**","answers/**","banners/**","bundle/**","users/**","publishers/**")
				.addResourceLocations("/resources/",("file:"+imageLocation+"questions/"),("file:"+imageLocation+"answers/"),("file:"+imageLocation+"banners/"),("file:"+imageLocation+"bundle/"),("file:"+imageLocation+"users/"),("file:"+imageLocation+"publishers/"));
	}

	@Bean
	public MultipartResolver multipartResolver() {
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		return resolver;
	}

}
