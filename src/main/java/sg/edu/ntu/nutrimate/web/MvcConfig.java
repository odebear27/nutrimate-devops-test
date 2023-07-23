// package sg.edu.ntu.nutrimate.web;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class MvcConfig implements WebMvcConfigurer {

// 	public void addViewControllers(ViewControllerRegistry registry) {
// 		registry.addViewController("/").setViewName("index");
// 		registry.addViewController("/index").setViewName("index");
// 		registry.addViewController("/auth-error").setViewName("authError");
// 		registry.addViewController("/signin").setViewName("signin");
// 		registry.addViewController("/signin-error").setViewName("signin");
// 		// registry.addViewController("/hello").setViewName("hello");
// 		// registry.addViewController("/login").setViewName("login");
// 	}

// }