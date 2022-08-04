package com.yrys.onlineshop.config;

import com.yrys.onlineshop.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    private EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // disable methods: PUT, POST, and DELETE
        HttpMethod[] unsupportedMethods = {HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PATCH};

        disableHttpMethods(ProductCategory.class, config, unsupportedMethods);
        disableHttpMethods(Product.class, config, unsupportedMethods);
        disableHttpMethods(Country.class, config, unsupportedMethods);
        disableHttpMethods(State.class, config, unsupportedMethods);
        disableHttpMethods(Order.class, config, unsupportedMethods);

        exposeIds(config);

        cors.addMapping(config.getBasePath() + "/**").allowedOrigins(allowedOrigins);
    }

    private void disableHttpMethods (Class theClass, RepositoryRestConfiguration config, HttpMethod[] unsupportedMethods) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(unsupportedMethods));
    }

    // TO EXPOSE ENTITY IDS
    private void exposeIds(RepositoryRestConfiguration config) {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        List<Class> entityClasses = new ArrayList<>();

        for (EntityType entityType: entities) {
            entityClasses.add(entityType.getJavaType());
        }

        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
    
}
