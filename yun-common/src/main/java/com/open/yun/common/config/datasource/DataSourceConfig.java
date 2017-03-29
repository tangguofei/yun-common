package com.open.yun.common.config.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.open.yun.common.utils.DESUtils;

/**
 * 重写DataSource,以满足数据库密码加密的需求
 * @author tang
 */
@Configuration
public class DataSourceConfig {
	@Autowired
	private DataSourceProperties properties;
	
	@Bean
	@Primary
	public DataSource dataSource() {
		DataSourceBuilder factory = DataSourceBuilder
				.create(this.properties.getClassLoader())
				.driverClassName(this.properties.getDriverClassName())
				.url(this.properties.getUrl()).username(this.properties.getUsername())
				.password(DESUtils.decrypt(properties.getPassword(), null));
		if (this.properties.getType() != null) {
			factory.type(this.properties.getType());
		}
		return factory.build();
	}
	
	public static void main(String[] args) {
		System.out.println(DESUtils.decrypt("xKAbUALCHQdPVPZxnwobpkfRUayoGJS9",
				null));
	}
}
