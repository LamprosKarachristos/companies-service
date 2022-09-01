package gr.quarkus.tutorials.repositories

import gr.quarkus.tutorials.entities.Location
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class LocationRepository : PanacheRepository<Location> {
}