package gr.quarkus.tutorials.repositories

import gr.quarkus.tutorials.entities.Phone
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class PhoneRepository : PanacheRepository<Phone> {}