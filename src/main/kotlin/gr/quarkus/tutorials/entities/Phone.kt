package gr.quarkus.tutorials.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.persistence.PrePersist

@Entity
class Phone : PanacheEntity() {

    @Column(unique = true)
    var number: String = ""
    var created: LocalDateTime? = null

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Company::class)
    @JsonBackReference(value = "company-phones")
    var company: Company? = null

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Employee::class)
    @JsonBackReference(value = "employee-phones")
    var employee: Employee? = null

    @PrePersist
    fun addCreateTime() {
        created = LocalDateTime.now()
    }

}