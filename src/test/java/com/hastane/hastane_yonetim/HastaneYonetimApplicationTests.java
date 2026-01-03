package com.hastane.hastane_yonetim;

import com.hastane.hastane_yonetim.TestcontainersConfiguration;
import org.springframework.context.annotation.Import;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class HastaneYonetimApplicationTests {

	@Test
	void contextLoads() {
	}

}
