package gr.quarkus.tutorials.repositories

import gr.quarkus.tutorials.entities.Company
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import io.quarkus.panache.common.Page
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CompanyRepository : PanacheRepository<Company> {
    fun getPage(page: Int, size: Int): Uni<MutableList<Company>> = findAll().page<Company>(Page.of(page, size)).list()

}