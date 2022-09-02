package gr.quarkus.tutorials.resourcses

import gr.quarkus.tutorials.entities.Company
import gr.quarkus.tutorials.entities.Email
import gr.quarkus.tutorials.entities.Location
import gr.quarkus.tutorials.entities.Phone
import gr.quarkus.tutorials.services.CompanyService
import gr.quarkus.tutorials.services.InformationService
import io.vertx.core.json.JsonObject
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/company")
class CompanyResource(val service: CompanyService, val information: InformationService) {

    //Company call methods
    @GET
    @RolesAllowed("admin", "user")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAll(@QueryParam("page") page: Int = 1, @QueryParam("size") size: Int = 3): Response =
        service.getAllCompanies(page, size)

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin", "user", "writer")
    @Path("/{id}")
    suspend fun getOne(@PathParam("id") id: Long): Response = service.getOneCompany(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin", "writer")
    suspend fun create(company: Company): Response = service.createCompany(company)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin", "writer")
    suspend fun update(company: Company): Response = service.updateCompany(company)

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin", "writer")
    suspend fun update(@PathParam("id") id: Long, updates: JsonObject): Response =
        service.updateCompanyPartly(id, updates)

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin")
    suspend fun delete(@PathParam("id") id: Long): Response = service.deleteCompany(id)

    //Phone call-methods for companies
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/phones")
    @RolesAllowed("admin", "user", "writer")
    suspend fun getPhones(@PathParam("id") id: Long): Response = service.getPhones(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}/phones")
    @RolesAllowed("admin", "writer")
    suspend fun addPhone(@PathParam("id") id: Long, phone: Phone): Response =
        information.addPhone(phone, id, "company")

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin")
    @Path("{company_id}/phones/{id}")
    suspend fun deletePhone(@PathParam("company_id") company_id: Long, @PathParam("id") id: Long): Response =
        information.deletePhone(company_id, id, "company")

    //Location call-Methods for Companies
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/location")
    @RolesAllowed("admin", "user", "writer")
    suspend fun getLocation(@PathParam("id") id: Long): Response = service.getLocations(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/location")
    @RolesAllowed("admin", "writer")
    suspend fun addLocation(@PathParam("id") id: Long, location: Location): Response =
        information.addLocation(location, id)

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{company_id}/location/{id}")
    @RolesAllowed("admin")
    suspend fun deleteLocation(@PathParam("company_id") company_id: Long, @PathParam("id") id: Long): Response =
        information.deleteLocation(company_id, id)

    //Email call-methods for companies
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/email")
    @RolesAllowed("admin", "user", "writer")
    suspend fun getEmails(@PathParam("id") id: Long): Response = service.getEmails(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/email")
    @RolesAllowed("admin", "writer")
    suspend fun addEmail(@PathParam("id") id: Long, email: Email): Response = information.addEmail(id, email, "company")

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{company_id}/email/{id}")
    @RolesAllowed("admin")
    suspend fun deleteEmail(@PathParam("company_id") company_id: Long, @PathParam("id") id: Long): Response =
        information.deleteEmail(company_id, id, "company")

//    @ServerExceptionMapper
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    fun handler(exception: Exception): Response? {
//        Log.error(exception.message)
//        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.message).build()
//    }

}