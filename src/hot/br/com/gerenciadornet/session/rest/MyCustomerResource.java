package br.com.gerenciadornet.session.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/customer")
public class MyCustomerResource {


    @GET
    @Produces("text/plain")
    public String getCustomer() {

         return "Hello Seam Reast...";

    }


}