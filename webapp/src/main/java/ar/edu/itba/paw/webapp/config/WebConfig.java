package ar.edu.itba.paw.webapp.config;


import ar.edu.itba.paw.webapp.locale.UserLocaleResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

@EnableTransactionManagement
@PropertySource("classpath:application${spring.profiles.active}.properties")
@EnableWebMvc
@ComponentScan({
        "ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.webapp.locale",
        "ar.edu.itba.paw.services",
        "ar.edu.itba.paw.persistence"
})
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Value("classpath:schema.sql")
    private Resource schemaSql;

    @Value("#{environment.dataBaseURL}")
    private String dataBaseURL;

    @Value("#{environment.dataBaseUser}")
    private String dataBaseUser;

    @Value("#{environment.dataBasePassword}")
    private String dataBasePassword;

    @Value("#{environment.inProduction}")
    private boolean inProduction;

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver vr = new InternalResourceViewResolver();

        vr.setViewClass(JstlView.class);
        vr.setPrefix("/WEB-INF/jsp/");
        vr.setSuffix(".jsp");

        return vr;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource ms = new
                ReloadableResourceBundleMessageSource();
        ms.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        ms.setBasename("classpath:i18n/messages");
        ms.setFallbackToSystemLocale(false);
        return ms;
    }

    @Bean
    public DataSource dataSource(Environment environment) {
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl(dataBaseURL);
        ds.setUsername(dataBaseUser);
        ds.setPassword(dataBasePassword);

        return ds;
    }

    @Bean
    public LocaleChangeInterceptor localeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }


    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new UserLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(localeInterceptor());
    }

    @Bean
    public DataSourceInitializer dsInitializer(final DataSource ds) {
        final DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(ds);
        dsi.setDatabasePopulator(dsPopulator());
        return dsi;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(final DataSource ds) {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.models");
        factoryBean.setDataSource(ds);

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        final Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");


//        if(!inProduction) {
//            jpaProperties.setProperty("hibernate.show_sql", "true");
//            jpaProperties.setProperty("hibernate.format_sql", "true");
//        }

        factoryBean.setJpaProperties(jpaProperties);
        return factoryBean;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760);
        return multipartResolver;
    }

    private DatabasePopulator dsPopulator() {
        ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
        dbp.addScript(schemaSql);
        return dbp;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
