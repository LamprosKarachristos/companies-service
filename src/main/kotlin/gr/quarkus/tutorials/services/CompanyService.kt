package gr.quarkus.tutorials.services

import gr.quarkus.tutorials.entities.Company
import gr.quarkus.tutorials.repositories.CompanyRepository
import io.quarkus.logging.Log
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.core.json.JsonObject
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class CompanyService(val repository: CompanyRepository) {

    suspend fun getAllCompanies(page: Int, size: Int): Response = repository.getPage(page, size)
        .onItem().transform { Response.ok(it).build() }.awaitSuspending()

    suspend fun createCompany(company: Company): Response =
        repository.persistAndFlush(company)
            .onItem().transform { Response.ok(it).build() }
            .onFailure().recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
            .awaitSuspending()

    suspend fun getOneCompany(id: Long): Response =
        repository.findById(id)
            .onItem().ifNotNull().transform { Response.ok(it).build() }
            .replaceIfNullWith(Response.status(Response.Status.NOT_FOUND).build()).awaitSuspending()

    suspend fun updateCompany(company: Company): Response =
        repository.update(
            "name=?1, owner=?2, site=?3 where id=?4",
            company.name,
            company.owner,
            company.site,
            company.id
        ).onItem()
            .transform {
                if (it == 1) Response.ok(company).build() else Response.status(Response.Status.BAD_REQUEST).build()
            }
            .awaitSuspending()

    suspend fun updateCompanyPartly(id: Long, updates: JsonObject): Response {
        val query: String = updates.joinToString(separator = " , ") {
            if (it.value is String) {
                "${it.key}='${it.value}'"
            } else {
                "${it.key}=${it.value}"
            }
        } + " where id=$id"
        Log.info(query)
        return repository.update(query)
            .onItem().transform {
                if (it.equals(1)) Response.ok().build() else Response.status(Response.Status.NOT_FOUND).build()
            }.awaitSuspending()
    }

    suspend fun deleteCompany(id: Long): Response =
        repository.deleteById(id)
            .onItem()
            .transform { if (it) Response.ok().build() else Response.status(Response.Status.BAD_REQUEST).build() }
            .call { _-> repository.flush() }
            .awaitSuspending()


    /*
    * Information Getters for :
    * @Phones
    * @Emails
    * @Locations
    */

    suspend fun getPhones(id: Long): Response =
        repository.findById(id)
            .onItem().transform { Response.ok(it.phones).build() }
            .awaitSuspending()

    suspend fun getLocations(id: Long): Response =
        repository.findById(id)
            .onItem().transform { Response.ok(it.locations).build() }
            .awaitSuspending()

    suspend fun getEmails(id: Long): Response =
        repository.findById(id)
            .onItem().transform { Response.ok(it.emails).build() }
            .awaitSuspending()
}