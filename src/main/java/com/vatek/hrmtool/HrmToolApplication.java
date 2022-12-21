package com.vatek.hrmtool;

import com.vatek.hrmtool.service.UserService;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
@EnableAsync
@Log4j2
@AllArgsConstructor
@EntityScan(basePackages = {"com.vatek.hrmtool"})
public class HrmToolApplication {

  final UserService userService;

  final Environment env;

  public static void main(String[] args) {
    SpringApplication.run(HrmToolApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void setApplicationTimeZone(){
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    log.info("TimeZone : {} , Instant : {} , Timestamp : {}", TimeZone::getDefault, Instant::now,() -> Timestamp.from(DateUtil.getInstantNow()));
  }

  @EventListener(ApplicationReadyEvent.class)
  public void clearToken(){
    log.info("Clear Token After Start Application : {}", () -> "clear Token");
    userService.clearAllToken();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void version(){
    log.info("Application Name : {}", () -> env.getProperty("application.name"));
    log.info("Build Version : {}", () -> env.getProperty("build.version"));
    log.info("Build Timestamp : {}", () -> env.getProperty("build.timestamp"));
    userService.clearAllToken();
  }
}
