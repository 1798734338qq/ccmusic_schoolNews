package cn.net.comsys.schoolNews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SchoolNewsApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SchoolNewsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(SchoolNewsApplication.class);
	}
}
