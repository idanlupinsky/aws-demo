package com.aws.demo.service;

import com.aws.demo.service.config.AwsDemoConfiguration;
import com.google.inject.AbstractModule;
import io.dropwizard.setup.Environment;

public class AwsDemoModule extends AbstractModule {
    private final Environment environment;
    private final AwsDemoConfiguration configuration;

    public AwsDemoModule(Environment environment, AwsDemoConfiguration configuration) {
        this.environment = environment;
        this.configuration = configuration;
    }

    @Override
    protected void configure() {

    }
}
