package com.epam.resourceservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Configuration
public class SchemaInitializer {

    @Bean
    public boolean schemaCreator(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS resource");
        return true;
    }
}
