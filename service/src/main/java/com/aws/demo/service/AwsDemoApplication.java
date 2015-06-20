package com.aws.demo.service;

import com.aws.demo.service.config.AwsDemoConfiguration;
import com.aws.demo.service.config.annotations.Metrics;
import com.aws.demo.service.resources.CampaignResource;
import com.aws.demo.service.resources.HomeResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class AwsDemoApplication extends Application<AwsDemoConfiguration> {
    public static void main(String[] args) throws Exception {
        new AwsDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "AwsDemoApplication";
    }

    @Override
    public void run(AwsDemoConfiguration configuration, Environment environment) throws Exception {
        Injector injector = Guice.createInjector(
                new AwsDemoModule(environment, configuration),
                new MetricsInstrumentationModule(environment.metrics(), new AbstractMatcher<TypeLiteral<?>>() {
                    @Override
                    public boolean matches(TypeLiteral<?> typeLiteral) {
                        return typeLiteral.getRawType().isAnnotationPresent(Metrics.class);
                    }
                }));

        environment.jersey().register(injector.getInstance(HomeResource.class));
        environment.jersey().register(injector.getInstance(CampaignResource.class));
    }
}
