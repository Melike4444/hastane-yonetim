package com.hastane.hastane_yonetim;

import org.springframework.boot.SpringApplication;

public class TestHastaneYonetimApplication {

	public static void main(String[] args) {
		SpringApplication.from(HastaneYonetimApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
