package com.unip.tp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
@ComponentScan(basePackages = {"com.unip.tp2.controllers"})
public class Tp2Application {

	public static void main(String[] args) {
		File basedir = new File("users_workspace");

		if (!basedir.exists()) {
			System.out.println("Directory users_workspace created @ " + basedir.getAbsolutePath());
			basedir.mkdir();
			File canixDir = new File("users_workspace/canix");
			File unipDir = new File("users_workspace/unip");
			File unipDir2 = new File("users_workspace/unip/Documents");
			canixDir.mkdir();
			unipDir.mkdir();
			unipDir2.mkdir();
		}
		SpringApplication.run(Tp2Application.class, args);
	}
}
