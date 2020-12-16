package com.newsletter.axon;

import org.testcontainers.containers.PostgreSQLContainer;

public class AxonPostgresqlContainer extends PostgreSQLContainer<AxonPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:12.3";
    private static AxonPostgresqlContainer container;

    private AxonPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static AxonPostgresqlContainer getInstance() {
        if (container == null) {
            container = new AxonPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
