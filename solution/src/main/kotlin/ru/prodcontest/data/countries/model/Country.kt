package ru.prodcontest.data.countries.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "countries", uniqueConstraints = [
    UniqueConstraint(columnNames = ["alpha2"]),
    UniqueConstraint(columnNames = ["alpha3"])
])
data class Country(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonIgnore
    val id: Long,

    @Column(name = "name")
    val name: String,

    @Column(name = "alpha2")
    val alpha2: String,

    @Column(name = "alpha3")
    val alpha3: String,

    @Column(name = "region", nullable = true)
    val region: String?,

) {

    constructor() : this(0, "", "", "", "")

}
