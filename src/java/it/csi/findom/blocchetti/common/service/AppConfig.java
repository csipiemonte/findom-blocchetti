/*******************************************************************************
 * Copyright Regione Piemonte - 2020
 * SPDX-License-Identifier: EUPL-1.2-or-later
 ******************************************************************************/
package it.csi.findom.blocchetti.common.service;

import it.csi.util.beanlocatorfactory.ServiceBeanLocator;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement
public class AppConfig {
	//i blocchetti che necessitano di operazioni sul db (escluso operazioni su xml della domanda fatte da aggregatore)
	//transazionali definiscono il proprio service in questo package
	@Bean
	public DataSource PostgresDataSource() {
		return (DataSource) ServiceBeanLocator.getBeanByName("dataSource");
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(PostgresDataSource());
		return transactionManager;
	}
}
