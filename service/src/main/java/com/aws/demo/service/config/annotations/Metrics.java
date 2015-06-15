package com.aws.demo.service.config.annotations;

import java.lang.annotation.*;

/**
 * This annotation is used to mark classes as targets for metrics via Guice AOP.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Metrics {
}
