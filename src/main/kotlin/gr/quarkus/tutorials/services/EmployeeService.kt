package gr.quarkus.tutorials.services

import gr.quarkus.tutorials.entities.Employee
import gr.quarkus.tutorials.repositories.EmployeeRepository
import io.quarkus.logging.Log
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.core.json.JsonObject
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class EmployeeService(val repository: EmployeeRepository) {

    suspend fun getAllEmployees(page: Int, size: Int): Response =
        repository.getPage(page, size)
            .onItem().transform { Response.ok(it).build() }
            .awaitSuspending()

    suspend fun getOneEmployee(id: Long): Response =
        repository.findById(id)
            .onItem().ifNotNull().transform { Response.ok(it).build() }
            .replaceIfNullWith(Response.status(Response.Status.NOT_FOUND).build()).awaitSuspending()

    suspend fun createEmployee(employee: Employee): Response =
        repository.persistAndFlush(employee).onItem()
            .transform { Response.ok(it).build() }
            .onFailure().recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
            .awaitSuspending()

    suspend fun updateEmployee(employee: Employee): Response =
        repository.update(
            "firstName=?1, lastName=?2, fullName=?3, age=?4, status=?5, profession=?6 where id=?7",
            employee.firstName,
            employee.lastName,
            employee.fullName,
            employee.age,
            employee.status,
            employee.profession,
            employee.id
        ).onItem().transform {
            if (it != 0) Response.ok(employee).build() else Response.status(Response.Status.BAD_REQUEST).build()
        }
            .awaitSuspending()

    suspend fun updateEmployee(id: Long, updates: JsonObject): Response {
        val query: String = updates.joinToString(separator = " , ") {
            if (it.value is String) {
                "${it.key}='${it.value}'"
            } else {
                "${it.key}=${it.value}"
            }
        } + ", updated='${LocalDateTime.now()}'  where id=$id"
        Log.info(query)
        return repository.update(query)
            .onItem().transform {
                if (it != 0) Response.ok().build() else Response.status(Response.Status.NOT_FOUND).build()
            }.awaitSuspending()
    }

    suspend fun delete(id: Long): Response =
        repository.deleteById(id)
            .onItem()
            .transform { if (it) Response.ok().build() else Response.status(Response.Status.BAD_REQUEST).build() }
            .call { _-> repository.flush() }
            .awaitSuspending()

    /*
    * Information Getters for :
    * @Phones
    * @Emails
    */

    suspend fun getPhones(id: Long): Response =
        repository.findById(id)
            .onItem().transform { Response.ok(it.phones).build() }
            .awaitSuspending()

    suspend fun getEmails(id: Long): Response =
        repository.findById(id)
            .onItem().transform { Response.ok(it.emails).build() }
            .awaitSuspending()

}