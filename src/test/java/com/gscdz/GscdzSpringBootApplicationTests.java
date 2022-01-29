package com.gscdz;

import com.gscdz.Utils.MathOCRUtil;
import com.gscdz.model.CustomUser;
import com.gscdz.service.CustomUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

@SpringBootTest
@Slf4j
class GscdzSpringBootApplicationTests {

	@Autowired
	CustomUserService service;
	@Test
	void contextLoads() {
	}

}
