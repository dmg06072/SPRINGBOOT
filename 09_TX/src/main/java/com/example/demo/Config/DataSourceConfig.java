package com.example.demo.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // @Bean
    // public DataSource dataSource2(){
    // BasicDataSource dataSource = new BasicDataSource();
    // dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    // dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
    // @Value("${db.username}")
    // #
    // private String username;

    // #
    // @Value("${db.password}")
    // # private String password;
    //
    // dataSource.setInitialSize(5);//초기 연결개수
    // dataSource.setMaxTotal(10);//최대 연결 개수
    // dataSource.setMaxIdle(8);//최대 유휴 연결 수
    // dataSource.setMinIdle(3);//최소 유휴 연결 수
    //
    // return dataSource;
    // }

    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
//         @Value("${db.username}")
// # private String username;

// # @Value("${db.password}")
// # private String password;
        return dataSource;
    }

}
