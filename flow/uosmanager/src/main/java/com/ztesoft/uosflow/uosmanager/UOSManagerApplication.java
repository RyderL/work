package com.ztesoft.uosflow.uosmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({ "application-web.xml", "application-mvc.xml" })
public class UOSManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UOSManagerApplication.class, args);
	}

}
