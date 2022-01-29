package com.gscdz;


import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.gscdz.Utils.PhotoConverUtil;
import com.gscdz.Utils.TimeUtil;
import com.gscdz.dto.CustomUserInfoDTO;
import com.gscdz.dto.SolProblemDTO;
import com.gscdz.model.*;
import com.gscdz.repository.*;

import com.gscdz.service.CustomUserService;
import com.gscdz.service.SolProblemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;


@SpringBootApplication
@ServletComponentScan(basePackages = "com.gscdz.listener.**")
@Slf4j
public class GscdzSpringBootApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GscdzSpringBootApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(GscdzSpringBootApplication.class, args);
	}

}
