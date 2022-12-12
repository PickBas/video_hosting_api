package com.therearenotasksforus.videohostingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VideoHostingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoHostingApiApplication.class, args);
	}

}
