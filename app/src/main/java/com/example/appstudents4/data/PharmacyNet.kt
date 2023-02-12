package com.example.appstudents4.data

import android.icu.util.GregorianCalendar
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.appstudents.data.Pharmacy
import java.util.*

@Entity(
    tableName = "pharmacyNet",
    foreignKeys = [ForeignKey(
        entity = Pharmacy::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("phamId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["number", "phamId"], unique = true)]
)
data class PharmacyNet(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var phamId: UUID = UUID.randomUUID(),
    var address: String = "",
    var number: Int = 0,
    var openingHours: String = "",
    var birthDate: Date = Date(),
    var employees: String = "",
    var city: String = "",
    var square: String = "",
    var attendance: String = "",
) {
    val age: Int
        get() {
            val gregorianCalendar1 = GregorianCalendar()
            gregorianCalendar1.timeInMillis = birthDate.time
            val gregorianCalendar2 = GregorianCalendar()
            var y = gregorianCalendar2.get(GregorianCalendar.YEAR) - gregorianCalendar1.get(
                GregorianCalendar.YEAR
            )
            if (gregorianCalendar1.get(GregorianCalendar.MONTH) < gregorianCalendar2.get(
                    GregorianCalendar.MONTH
                )
            )
                y--
            return y
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PharmacyNet

        if (id != other.id) return false
        if (phamId != other.phamId) return false
        if (address != other.address) return false
        if (number != other.number) return false
        if (openingHours != other.openingHours) return false
        if (birthDate != other.birthDate) return false
        if (employees != other.employees) return false
        if (city != other.city) return false
        if (square != other.square) return false
        if (attendance != other.attendance) return false
        if (age != other.age) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + phamId.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + openingHours.hashCode()
        result = 31 * result + birthDate.hashCode()
        result = 31 * result + employees.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + square.hashCode()
        result = 31 * result + attendance.hashCode()
        result = 31 * result + age
        return result
    }


}
