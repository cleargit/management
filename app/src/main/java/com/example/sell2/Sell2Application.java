package com.example.sell2;

import com.example.sell2.quartz.Server.CreateScheduler;
import com.example.sell2.quartz.entity.CronSchedulerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableTransactionManagement
@EnableAsync
public class Sell2Application {

	public static void main(String[] args) {
		SpringApplication.run(Sell2Application.class, args);

	}

}
