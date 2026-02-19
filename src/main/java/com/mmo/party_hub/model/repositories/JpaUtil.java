package com.mmo.party_hub.model.repositories;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class JpaUtil {

	@Bean
	public DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();

	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUsername("root");
	    dataSource.setPassword("root");
	    
	    dataSource.setUrl(
	      "jdbc:mysql://localhost:3306/koshinha?createDatabaseIfNotExist=true"); 
	 
	    return dataSource;
	}
	
}