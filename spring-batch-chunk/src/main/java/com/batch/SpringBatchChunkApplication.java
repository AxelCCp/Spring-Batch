package com.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class SpringBatchChunkApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchChunkApplication.class, args);
	}

	@Bean																												//para ejecutar el springbatch al levantar la app, se usa el CommandLineRunner.
	CommandLineRunner init(){
		return args -> {
			JobParameters jobParameters = new JobParametersBuilder()													//para mandar parametros
					.addString("name", "chunk")																			//un parametro.
					.addLong("id", System.currentTimeMillis())															//un id para cada job como parametro. sirve para diferenciar una ejecucion de otra, ya q sino, va a dar error y va a decir q ya hay una ejecucion creada previamente,
					.addDate("date", new Date())																		//le pasamos la fecha.
					.toJobParameters();

			jobLauncher.run(job, jobParameters);																		//le da run.
		};
	}

}
