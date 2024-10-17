package com.wavemaker.employee.util;


import com.mysql.cj.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DBConfig {
    private static final Logger logger = LoggerFactory.getLogger(DBConfig.class);
    //constants
    private final static String DB_URL = "jdbc:mysql://127.0.0.1:3306/employee_leave_management";
    private final static String DB_USERNAME = "root";
    private final static String DB_PASSWORD = "root123";

    @Bean
    public Connection getDBConnectionInstance() throws SQLException {
        try {
            DriverManager.registerDriver(new Driver());
            logger.info("Database connection established successfully.");
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            logger.error("Error establishing database connection: ", e);
        }
        return null;
    }
}
