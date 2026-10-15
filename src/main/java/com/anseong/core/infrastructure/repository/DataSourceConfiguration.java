package com.anseong.core.infrastructure.repository;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableTransactionManagement
// @EnableJpaRepositories(basePackages = { "com.anseong" })
class DataSourceConfiguration {

     // private final DataSource lazyRoutingDataSource;

     // DataSourceConfiguration(DataSource lazyRoutingDataSource) {
     //      this.lazyRoutingDataSource = lazyRoutingDataSource;
     // }

     @Bean
     @ConfigurationProperties(prefix = "spring.datasource.master")
     DataSource masterDataSource() {
          return DataSourceBuilder.create().type(HikariDataSource.class).build();
     }

     @Bean
     @ConfigurationProperties(prefix = "spring.datasource.replica")
     DataSource replicaDataSource() {
          return DataSourceBuilder.create().type(HikariDataSource.class).build();
     }

     @Bean
     DataSource routingDataSource(
               @Qualifier("masterDataSource") DataSource masterDataSource,
               @Qualifier("replicaDataSource") DataSource replicaDataSource
     ) {
          var routingDataSource = new AbstractRoutingDataSource() {

               @Override
               protected Object determineCurrentLookupKey() {
                    return TransactionSynchronizationManager.isCurrentTransactionReadOnly()
                         ? "replica"
                         : "master";
               }
          };

          Map<Object, Object> targetDataSources = new HashMap<>();
          targetDataSources.put("master", masterDataSource);
          targetDataSources.put("replica", replicaDataSource);

          routingDataSource.setTargetDataSources(targetDataSources);
          routingDataSource.setDefaultTargetDataSource(masterDataSource());

          return routingDataSource;
     }

     @Bean
     @Primary
     DataSource lazyRoutingDataSource(
               @Qualifier("routingDataSource") DataSource routingDataSource
     ) {
          return new LazyConnectionDataSourceProxy(routingDataSource);
     }

     // @PersistenceContext
     // private EntityManager entityManager;

     @Bean
     JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
          return new JPAQueryFactory(entityManager);
     }

     // @Bean
     // public JdbcTemplate jdbcTemplate() {
     //      return new JdbcTemplate(lazyRoutingDataSource());
     // }

     // @Bean
     // public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
     //      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
     //      entityManagerFactoryBean.setDataSource(lazyRoutingDataSource());
     //      entityManagerFactoryBean.setPackagesToScan("com.anseong.domain.model");
     //      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
     //      entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

     //      return entityManagerFactoryBean;
     // }

     // @Bean
     // public PlatformTransactionManager transactionManager(
     //      @Qualifier("lazyRoutingDataSource") DataSource lazyRoutingDataSource
     // ) {
     //      DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
     //      transactionManager.setDataSource(lazyRoutingDataSource);
     //      return transactionManager;
     // }
}
