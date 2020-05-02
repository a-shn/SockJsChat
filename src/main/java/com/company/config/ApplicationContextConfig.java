package com.company.config;

import com.company.repositories.*;
import com.company.services.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.sql.DataSource;
import java.util.Objects;

@Component
@Configuration
@ComponentScan(basePackages = "com.company")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class ApplicationContextConfig {
    @Autowired
    private Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionsService sessionsService() {
        return new SessionsServiceImpl();
    }

    @Bean
    public SignInService signInService() {
        return new SignInServiceImpl(userRepository());
    }

    @Bean
    public SignUpService signUpService() {
        return new SignUpServiceImpl(userRepository());
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftlh");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(Objects.requireNonNull(freeMarkerConfiguration()));
        return freeMarkerConfigurer;
    }

    @Bean
    public freemarker.template.Configuration freeMarkerConfiguration() {
        return (freemarker.template.Configuration) freeMarkerConfigurationFactoryBean().getObject();
    }

    @Bean
    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
        FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
        freeMarkerFactoryBean.setTemplateLoaderPath("/WEB-INF/templates/");
        freeMarkerFactoryBean.setPreferFileSystemAccess(true);
        freeMarkerFactoryBean.setDefaultEncoding("UTF-8");
        return freeMarkerFactoryBean;
    }

    @Bean
    public SessionsRepository sessionsRepository() {
        return new SessionsRepositoryImpl(jdbcTemplate());
    }

    @Bean
    public ChatRepository chatRepository() {
        return new ChatJdbcTemplateImpl(jdbcTemplate());
    }

    @Bean
    public UsersRepository userRepository() {
        return new UsersJdbcTemplateImpl(jdbcTemplate());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty("db.url"));
        config.setUsername(environment.getProperty("db.user"));
        config.setPassword(environment.getProperty("db.password"));
        config.setDriverClassName(environment.getProperty("db.driver"));
        return config;
    }

    @Bean
    public DataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }
}
