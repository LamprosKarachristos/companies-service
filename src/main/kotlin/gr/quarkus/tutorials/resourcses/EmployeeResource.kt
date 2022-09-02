package gr.quarkus.tutorials.resourcses

import gr.quarkus.tutorials.entities.Email
import gr.quarkus.tutorials.entities.Employee
import gr.quarkus.tutorials.entities.Phone
import gr.quarkus.tutorials.services.EmployeeService
import gr.quarkus.tutorials.services.InformationService
import io.quarkus.logging.Log
import io.vertx.core.json.JsonObject
import org.jboss.resteasy.reactive.server.ServerExceptionMapper
import javax.annotation.security.RolesAllowed
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


@Path("/employee")
class EmployeeResource(val service: EmployeeService, val information: InformationService) {

    @GET
    @RolesAllowed("admin", "user", "writer")
    @Produces(MediaType.APPLICATION_JSON)
    suspend fun getAll(@QueryParam("page") page: Int = 1, @QueryParam("size") size: Int = 3): Response =
        service.getAllEmployees(page, size)

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("admin", "user", "writer")
    suspend fun getOne(@PathParam("id") id: Long): Response = service.getOneEmployee(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin", "writer")
    suspend fun create(employee: Employee): Response = service.createEmployee(employee)

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin", "writer")
    suspend fun update(employee: Employee): Response = service.updateEmployee(employee)

    @PATCH
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("admin", "writer")
    suspend fun update(@PathParam("id") id: Long, updates: JsonObject): Response = service.updateEmployee(id, updates)


    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    suspend fun delete(@PathParam("id") id: Long): Response = service.delete(id)

    //Phone call methods for employees
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/phones")
    @RolesAllowed("admin", "user", "writer")
    suspend fun getPhones(@PathParam("id") id: Long): Response = service.getPhones(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/phones")
    @RolesAllowed("admin", "writer")
    suspend fun addPhone(phone: Phone, @PathParam("id") id: Long): Response =
        information.addPhone(phone, id, "employee")

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{employee_id}/phones/{id}")
    @RolesAllowed("admin")
    suspend fun deletePhone(@PathParam("employee_id") employee_id: Long, @PathParam("id") id: Long): Response =
        information.deletePhone(employee_id, id, "employee")

    //Email call methods for employees
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/emails")
    @RolesAllowed("admin", "user", "writer")
    suspend fun getEmails(@PathParam("id") id: Long): Response = service.getEmails(id)

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}/emails")
    @RolesAllowed("admin", "writer")
    suspend fun addEmail(@PathParam("id") id: Long, email: Email): Response =
        information.addEmail(id, email, "employee")

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{employee_id}/emails/{id}")
    @RolesAllowed("admin")
    suspend fun deleteEmail(@PathParam("employee_id") employee_id: Long, @PathParam("id") id: Long): Response =
        information.deleteEmail(employee_id, id, "employee")

    @ServerExceptionMapper
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    fun handler(exception: Exception): Response? {
        Log.error(exception.message)
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.message).build()
    }

}