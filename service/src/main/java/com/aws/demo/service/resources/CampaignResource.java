package com.aws.demo.service.resources;

import com.aws.demo.service.domain.Campaign;
import com.codahale.metrics.annotation.Timed;
import org.joda.time.DateTime;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/campaigns")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class CampaignResource {
    @GET
    @Path("{id}")
    @Timed
    public Campaign getCampaign(@PathParam("id") int id) {
        return new Campaign(id, "MyCampaign", DateTime.now());
    }
}
