package com.batch.config;

import com.batch.steps.ItemReaderStep2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfiguration {

    @Bean
    public ItemReaderStep2 itemReaderStep2(){
        return new ItemReaderStep2();
    }
}
