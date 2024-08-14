package com.example.assessment.configs;

import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
public class DatabaseInitializer {

    private final DatabaseClient databaseClient;

    public DatabaseInitializer(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @PostConstruct
    public void initialize() throws Exception {
        String schema = StreamUtils.copyToString(new ClassPathResource("schema.sql").getInputStream(),
                StandardCharsets.UTF_8);
        databaseClient.sql(schema).then().subscribe();
    }
}
