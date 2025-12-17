package com.example.bankcards.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Configuration
@EnableCaching
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public DateTimeFormatter expiringDateFormatter() {
        return DateTimeFormatter.ofPattern("MM/yyyy");
    }

    @Bean
    public Pattern serialLastDigitsPattern() {
        String regex = "^\\d{4}$";
        return Pattern.compile(regex);
    }
}
