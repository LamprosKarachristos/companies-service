package gr.quarkus.tutorials.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.persistence.*

@Entity
class Location : PanacheEntity() {
    var address: String = ""
    var created: LocalDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Company::class)
    @JsonBackReference(value = "company-locations")
    var company: Company? = null

    @PrePersist
    fun addCreateTime() {
        created = LocalDateTime.now()
    }

}