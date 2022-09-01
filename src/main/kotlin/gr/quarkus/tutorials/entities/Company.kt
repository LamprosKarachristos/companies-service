package gr.quarkus.tutorials.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import io.quarkus.logging.Log
import io.quarkus.scheduler.Scheduled
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.jetbrains.annotations.Nullable
import java.time.LocalDateTime
import java.util.Date
import javax.enterprise.context.ApplicationScoped
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@Entity
class Company : PanacheEntity() {
    var owner: String = ""
    var name: String = ""

    @Column(unique = true)
    var site: String = ""

    //var established : Date? = null
    var created: LocalDateTime? = null
    var updated: LocalDateTime? = null

    @Nullable
    @JsonManagedReference(value = "Employee")
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var employees: Set<Employee>? = setOf()


    @Fetch(FetchMode.JOIN)
    @JsonManagedReference(value = "company-locations")
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true)
    var locations: Set<Location>? = setOf()

    @Nullable
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference(value = "company-phones")
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true)
    var phones: Set<Phone>? = setOf()


    @Nullable
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference(value = "company-emails")
    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var emails: Set<Email>? = setOf()

    @PrePersist
    fun addCreateTime() {
        created = LocalDateTime.now()
        updated = LocalDateTime.now()
    }

    @PreUpdate
    fun addUpdateTime() {
        updated = LocalDateTime.now()
    }

}