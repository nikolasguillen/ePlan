package com.example.eplan.network.model

import com.example.eplan.domain.model.*
import com.example.eplan.domain.util.DomainMapper
import com.example.eplan.domain.util.Periodicity
import com.example.eplan.domain.util.WarningUnit
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
        val startDateTime = "${domainModel.date} ${domainModel.start}"
        val endDateTime = "${domainModel.date} ${domainModel.end}"
        val invited = mutableListOf<User>()
        domainModel.invited.forEach {
            invited.add(it.key)
        }

        return AppointmentDto(
            idAppuntamento = domainModel.id,
            idAttivita = domainModel.activityId,
            title = domainModel.title,
            description = domainModel.description,
            start = startDateTime,
            end = endDateTime,
            planning = domainModel.planning,
            intervention = domainModel.intervention,
            invited = invited,
            periodicity = domainModel.periodicity.name,
            periodicityEnd = domainModel.periodicityEnd.toString(),
            memo = domainModel.memo,
            memoType = domainModel.memoType,
            warningTime = domainModel.warningTime,
            warningUnit = domainModel.warningUnit.name
        )
    }

    fun toDomainList(initial: List<AppointmentDto>): List<Appointment> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Appointment>): List<AppointmentDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}