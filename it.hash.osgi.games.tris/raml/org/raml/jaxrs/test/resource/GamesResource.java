
package org.raml.jaxrs.test.resource;

import java.io.Reader;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("games")
public interface GamesResource {


    /**
     * subscribe
     * ...
     * 
     * 
     * @param clientid
     *     
     * @param entity
     *     
     */
    @POST
    @Path("1.0/tris/player/{clientid}/action")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    Response postGames10TrisPlayerByClientidAction(
        @PathParam("clientid")
        @NotNull
        String clientid, Reader entity);

    /**
     * subscribe
     * ...
     * 
     * 
     * @param clientid
     *     
     * @param entity
     *     
     */
    @POST
    @Path("1.0/tris/player/{clientid}/subscribe")
    @Consumes("application/json")
    @Produces({
        "application/json"
    })
    Response postGames10TrisPlayerByClientidSubscribe(
        @PathParam("clientid")
        @NotNull
        String clientid, Reader entity);

}
