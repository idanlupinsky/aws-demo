package com.aws.demo.service.resources;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
@Singleton
public class HomeResource {
    @GET
    public String get() {
        return "AWS Demo Service";
    }
}
