package ar.edu.itba.paw.persistence.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@ComponentScan({"ar.edu.itba.paw.persistence", "ar.edu.itba.paw.interfaces.services", "ar.edu.itba.paw.services"})
@EnableTransactionManagement
public class TestConfig {
	
	@Value("classpath:pgsql.sql")
	private Resource pgSql;
	
	@Value("classpath:schema.sql")
	private Resource schemaSql;
	
	@Value("classpath:insertions.sql")
	private Resource insertionsSql;
    
	@Bean
	public DataSource dataSource() {

		final SimpleDriverDataSource ds = new SimpleDriverDataSource();
		
		ds.setDriverClass(org.hsqldb.jdbc.JDBCDriver.class);
		ds.setUrl("jdbc:hsqldb:mem:paw");
		ds.setUsername("ha");
		ds.setPassword("");
		
		return ds;
	}
	
	@Bean
	public DataSourceInitializer dsInitializer(final DataSource ds) {

		final DataSourceInitializer dsi = new DataSourceInitializer();
		
		dsi.setDataSource(ds);
		dsi.setDatabasePopulator(dsPopulator());
		
		return dsi;
	}
	
	private DatabasePopulator dsPopulator() {
		
		final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
		
		dbp.addScript(pgSql);
		dbp.addScript(schemaSql);
//		dbp.addScript(insertionsSql);
		
		return dbp;
	}
	
	@Bean
	public TransactionManager transactionManager(DataSource ds) {
		
		return new JdbcTransactionManager(ds);
	}
		
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		
	final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
	
		factoryBean.setPackagesToScan("ar.edu.itba.paw.model");
		factoryBean.setDataSource(dataSource());
		
		final HibernateJpaVendorAdapter jpaAdapter = new HibernateJpaVendorAdapter();
		factoryBean.setJpaVendorAdapter(jpaAdapter);
		
		final Properties properties = new Properties();
		
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		properties.setProperty("hibernate.hbm2ddl.auto", "none");
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.format_sql", "true");
		factoryBean.setJpaProperties(properties);
		
		return factoryBean;
	}
	
}
