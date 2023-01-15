package net.vorlon.iqfamily.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class LiquibaseConfig {

    private final DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase-changeLog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }

}
