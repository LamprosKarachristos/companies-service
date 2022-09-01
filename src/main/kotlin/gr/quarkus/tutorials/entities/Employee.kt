package gr.quarkus.tutorials.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.jetbrains.annotations.Nullable
import java.time.LocalDateTime
import javax.persistence.*


@Entity
class Employee : PanacheEntity() {
    var firstName: String = ""
    var lastName: String = ""
    var fullName: String = ""
    var age: Int = 0
    var status: Status? = null
    var profession: Profession? = null
    var created: LocalDateTime? = null
    var updated: LocalDateTime? = null

    @Nullable
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "employee", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "employee-phones")
    var phones: Set<Phone>? = setOf()

    @Nullable
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "employee", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "employee-emails")
    var emails: Set<Email>? = setOf()

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Company::class)
    @JsonBackReference(value = "Employee")
    var company: Company? = null

    @PrePersist
    fun addCreateTime() {
        created = LocalDateTime.now()
        updated = LocalDateTime.now()
    }

    @PreUpdate
    fun addUpdateTime() {
        updated = LocalDateTime.now()
    }

    companion object : PanacheRepository<Employee>
}

enum class Status(val index: Int) {
    WORKING(1), NOT_WORKING(2);
}

enum class Profession(){
    BUSINESS_MANAGER, SECRETARY, TECHNICAL_LEADER, TECHNICIAN,
    WEB_DEVELOPER, DESIGNER, WORKER, CLEANER, DEVELOPER, TESTER
}