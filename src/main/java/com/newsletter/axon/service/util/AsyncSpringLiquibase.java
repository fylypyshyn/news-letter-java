package com.newsletter.axon.service.util;

import liquibase.exception.LiquibaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase;
import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executor;

public class AsyncSpringLiquibase extends DataSourceClosingSpringLiquibase {
    private final Logger logger = LoggerFactory.getLogger(AsyncSpringLiquibase.class);
    private final Executor executor;

    public AsyncSpringLiquibase(Executor executor) {
        this.executor = executor;
    }

    public void afterPropertiesSet() throws LiquibaseException {
        try {
            Connection connection = this.getDataSource().getConnection();

            try {
                this.executor.execute(() -> {
                    try {
                        this.logger.warn("Starting Liquibase asynchronously, your database might not be ready at startup!");
                        this.initDb();
                    } catch (LiquibaseException var2) {
                        this.logger.error("Liquibase could not start correctly, your database is NOT ready: {}", var2.getMessage(), var2);
                    }

                });
            } catch (Throwable var5) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }

                throw var5;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var6) {
            this.logger.error("Liquibase could not start correctly, your database is NOT ready: {}", var6.getMessage(), var6);
        }
    }


    protected void initDb() throws LiquibaseException {
        StopWatch watch = new StopWatch();
        watch.start();
        super.afterPropertiesSet();
        watch.stop();
        this.logger.debug("Liquibase has updated your database in {} ms", watch.getTotalTimeMillis());
        if (watch.getTotalTimeMillis() > 5000L) {
            this.logger.warn("Warning, Liquibase took more than {} seconds to start up!", 5L);
        }

    }
}
