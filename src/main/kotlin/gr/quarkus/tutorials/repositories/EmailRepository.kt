package gr.quarkus.tutorials.repositories

import gr.quarkus.tutorials.entities.Email
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped


@ApplicationScoped
class EmailRepository : PanacheRepository<Email> {
}