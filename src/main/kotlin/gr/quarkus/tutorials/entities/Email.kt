package gr.quarkus.tutorials.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import org.hibernate.annotations.CreationTimestamp
import org.jetbrains.annotations.Nullable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Email : PanacheEntity() {

    @Column(unique = true)
    var email: String = ""

    @CreationTimestamp
    var created: LocalDateTime? = null

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Company::class)
    @JsonBackReference(value = "company-emails")
    var company: Company? = null

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Employee::class)
    @JsonBackReference(value = "employee-emails")
    var employee: Employee? = null

    @PrePersist
    fun addCreateTime() {
        created = LocalDateTime.now()
    }
}