package com.spring.api.global.webapp.config;

import java.io.IOException;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@MapperScan("com.spring.api.domain.**.mapper")
public class DatabaseConfig {

	private final String driverClassName = "com.mysql.cj.jdbc.Driver";
	
	private final String typeAliasesPackage = "com.spring.api";
	
	private final String mapperLocations = "classpath:mappers/**/*.xml";
	
	@Value("${dataSource.username}")
	private String username;
	
	@Value("${dataSource.password}")
	private String password;
	
	@Value("${dataSource.jdbcUrl}")
	private String jdbcUrl;
	
	@Bean
	public HikariConfig hikariConfig() {
		HikariConfig config = new HikariConfig();
		config.setUsername(username);
		config.setPassword(password);
		config.setJdbcUrl(jdbcUrl);
		config.setDriverClassName(driverClassName);
		return config;
	}
	
	@Bean
	public HikariDataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}
	
	@Bean
	public org.apache.ibatis.session.Configuration myBatisConfig() {
		org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
		config.setMapUnderscoreToCamelCase(true);
		return config;
	}
	
	@Bean
	public SpringManagedTransactionFactory transactionFactory() {
		return new SpringManagedTransactionFactory();
	}
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setTransactionFactory(transactionFactory());
		factoryBean.setConfiguration(myBatisConfig());
		factoryBean.setTypeAliasesPackage(typeAliasesPackage);
		factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		return factoryBean;
	}
	
	@Bean public PlatformTransactionManager txManager() {
		DataSourceTransactionManager txMgr = new DataSourceTransactionManager(); 
		txMgr.setDataSource(dataSource()); 
		
		return txMgr; 
		
	}

	
	
}
