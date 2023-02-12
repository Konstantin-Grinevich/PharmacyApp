package com.example.appstudents.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Pharmacy (
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var name: String = ""
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pharmacy

        if (id != other.id) return false
        if (name != other.name) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}