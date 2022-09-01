package gr.quarkus.tutorials.repositories

import gr.quarkus.tutorials.entities.Employee
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.logging.Log
import io.quarkus.panache.common.Page
import io.quarkus.scheduler.Scheduled
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

@ApplicationScoped
class EmployeeRepository : PanacheRepository<Employee> {

    fun getPage(page: Int, size: Int): Uni<MutableList<Employee>> =
        findAll().page<Employee>(Page.of(page, size)).list()

}