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

package sg.edu.ntu.nutrimate.web;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.core.Ordered;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

     @Override
        public void addViewControllers(ViewControllerRegistry registry) {
            registry.setOrder(Ordered.LOWEST_PRECEDENCE);
            registry.addViewController("/**").setViewName("forward:/index.html");
        }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        Resource requestedResource = location.createRelative(resourcePath);
                        return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
                    }
                });
    }
}