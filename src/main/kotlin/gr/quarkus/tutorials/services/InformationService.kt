package gr.quarkus.tutorials.services

import gr.quarkus.tutorials.entities.*
import gr.quarkus.tutorials.repositories.EmailRepository
import gr.quarkus.tutorials.repositories.LocationRepository
import gr.quarkus.tutorials.repositories.PhoneRepository
import io.quarkus.logging.Log
import io.smallrye.mutiny.coroutines.awaitSuspending

import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response

@ApplicationScoped
class InformationService(
    val phoneRepository: PhoneRepository,
    val emailRepository: EmailRepository,
    val locationRepository: LocationRepository
) {

    //Phone services
    suspend fun addPhone(phone: Phone, id: Long, toEntity: String): Response {

        val company = Company()
        company.id = id
        val employee = Employee()
        employee.id = id

        when (toEntity) {
            "employee" -> phone.employee = employee
            "company" -> phone.company = company
            else -> Log.info("Entity not found")
        }
        return phoneRepository.persistAndFlush(phone)
            .onItem().transform { Response.ok(it).build() }
            .onFailure().recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
            .awaitSuspending()
    }

    suspend fun deletePhone(entity_id: Long, id: Long, toEntity: String): Response {
        var query: String = ""
        when (toEntity) {
            "company" -> query = "company_id=?1 AND id=?2"
            "employee" -> query = "employee_id=?1 AND id=?2"
        }

        return phoneRepository.delete(query, entity_id, id)
            .onItem()
            .transform {
                if (it == 1.toLong()) Response.ok().build() else Response.status(Response.Status.BAD_REQUEST).build()
            }
            .call { _-> phoneRepository.flush() }
            .awaitSuspending()
    }

    //Email services
    suspend fun addEmail(id: Long, email: Email, toEntity: String): Response {

        val company = Company()
        company.id = id
        val employee = Employee()
        employee.id = id

        when (toEntity) {
            "employee" -> email.employee = employee
            "company" -> email.company = company
            else -> Log.info("Entity not found")
        }

        return emailRepository.persistAndFlush(email)
            .onItem().transform { Response.ok(it).build() }
            .onFailure().recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
            .awaitSuspending()
    }

    suspend fun deleteEmail(entity_id: Long, id: Long, toEntity: String): Response {

        var query: String = ""
        when (toEntity) {
            "company" -> query = "company_id=?1 AND id=?2"
            "employee" -> query = "employee_id=?1 AND id=?2"
        }

        return emailRepository.delete(query, entity_id, id)
            .onItem()
            .transform {
                if (it == 1.toLong()) Response.ok().build() else Response.status(Response.Status.BAD_REQUEST).build()
            }
            .call { _-> emailRepository.flush() }
            .awaitSuspending()
    }

    //Location services
    suspend fun addLocation(location: Location, id: Long): Response {

        val company = Company()
        company.id = id
        location.company = company

        return locationRepository.persistAndFlush(location)
            .onItem().transform { Response.ok(it).build() }
            .onFailure().recoverWithItem(Response.status(Response.Status.BAD_REQUEST).build())
            .awaitSuspending()
    }

    suspend fun deleteLocation(company_id: Long, id: Long): Response =
        locationRepository.delete("company_id=?1 AND id=?2", company_id, id)
            .onItem()
            .transform {
                if (it == 1.toLong()) Response.ok().build() else Response.status(Response.Status.BAD_REQUEST).build()
            }
            .call { _-> locationRepository.flush() }
            .awaitSuspending()

}