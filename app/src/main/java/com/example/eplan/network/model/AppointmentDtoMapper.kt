package com.example.eplan.network.model

import com.example.eplan.domain.model.*
import com.example.eplan.domain.util.DomainMapper
import com.example.eplan.domain.util.Periodicity
import com.example.eplan.domain.util.WarningUnit
import com.example.eplan.network.util.dateTimeParser
import com.example.eplan.network.util.removeHtmlBreak
import java.time.LocalDate

class AppointmentDtoMapper : DomainMapper<AppointmentDto, Appointment> {

    override fun mapToDomainModel(model: AppointmentDto): Appointment {
        val startDateTime = dateTimeParser(model.start)
        val endDateTime = dateTimeParser(model.end)

        return Appointment(
            id = model.appointmentId,
            activityId = model.activityId,
            title = model.title,
            description = removeHtmlBreak(model.description),
            date = startDateTime.first,
            start = startDateTime.second,
            end = endDateTime.second,
            planning = model.planning,
            intervention = model.intervention,
            invited = model.invited,
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

        return AppointmentDto(
            appointmentId = domainModel.id,
            activityId = domainModel.activityId,
            title = domainModel.title,
            description = domainModel.description,
            start = startDateTime,
            end = endDateTime,
            planning = domainModel.planning,
            intervention = domainModel.intervention,
            invited = domainModel.invited,
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