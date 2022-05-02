package com.example.eplan.network.model

import com.example.eplan.domain.model.Appointment
import com.example.eplan.domain.model.Periodicity
import com.example.eplan.domain.model.User
import com.example.eplan.domain.model.WarningUnit
import com.example.eplan.domain.util.DomainMapper
import com.example.eplan.network.util.dateTimeParser
import java.time.LocalDate

class AppointmentDtoMapper : DomainMapper<AppointmentDto, Appointment> {

    override fun mapToDomainModel(model: AppointmentDto): Appointment {
        val startDateTime = dateTimeParser(model.start)
        val endDateTime = dateTimeParser(model.end)

        val invited = mutableMapOf<User, Boolean>()
        model.invited.forEach {
            invited[it] = true
        }

        return Appointment(
            id = model.idAppuntamento,
            activityId = model.idAttivita,
            title = model.title,
            description = model.description,
            date = startDateTime.first,
            start = startDateTime.second,
            end = endDateTime.second,
            planning = model.planning,
            intervention = model.intervention,
            invited = invited,
            periodicity = Periodicity.valueOf(model.periodicity),
            periodicityEnd = LocalDate.parse(model.periodicityEnd),
            memo = model.memo,
            memoType = model.memoType,
            warningTime = model.warningTime,
            warningUnit = WarningUnit.valueOf(model.warningUnit)
        )
    }

    override fun mapFromDomainModel(domainModel: Appointment): AppointmentDto {
        TODO("Not yet implemented")
    }
}