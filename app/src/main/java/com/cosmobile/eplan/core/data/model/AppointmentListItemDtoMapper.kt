package com.cosmobile.eplan.core.data.model

import com.cosmobile.eplan.appointment_detail.domain.model.Periodicity
import com.cosmobile.eplan.appointment_detail.domain.model.WarningUnit
import com.cosmobile.eplan.core.data.dateTimeParser
import com.cosmobile.eplan.core.data.removeHtmlBreak
import com.cosmobile.eplan.core.domain.model.Appointment
import com.cosmobile.eplan.core.domain.model.MemoType
import com.cosmobile.eplan.core.domain.util.DomainMapper
import java.time.LocalDate

class AppointmentDtoMapper(
    private val warningTimeConverter: WarningTimeConverter
) : DomainMapper<AppointmentListItemDto, Appointment> {

    override fun mapToDomainModel(model: AppointmentListItemDto): Appointment? {
        if (model.appointmentId == null || model.activityId == null || model.title == null || model.start == null || model.end == null) {
            return null
        }

        val startDateTime = dateTimeParser(model.start)
        val endDateTime = dateTimeParser(model.end)

        return Appointment(
            id = model.appointmentId,
            activityId = model.activityId,
            title = model.title,
            description = removeHtmlBreak(model.description ?: ""),
            date = startDateTime.first,
            start = startDateTime.second,
            end = endDateTime.second,
            planning = model.planning == 1,
            accounted = model.accounted == 1,
            invited = model.invited ?: emptyList(),
            periodicity = when (model.periodicity) {
                Periodicity.None.serverFieldName -> Periodicity.None
                Periodicity.Daily.serverFieldName -> Periodicity.Daily
                Periodicity.Weekly.serverFieldName -> Periodicity.Weekly
                Periodicity.Biweekly.serverFieldName -> Periodicity.Biweekly
                Periodicity.Monthly.serverFieldName -> Periodicity.Monthly
                Periodicity.Bimonthly.serverFieldName -> Periodicity.Bimonthly
                else -> Periodicity.None
            },
            periodicityEnd = model.periodicityEnd?.let { LocalDate.parse(it) },
            memo = model.memo ?: false,
            memoType = when (model.memoType) {
                MemoType.None.name -> MemoType.None
                MemoType.Email.name -> MemoType.Email
                MemoType.Notification.name -> MemoType.Notification
                else -> MemoType.None
            },
            warningTime = model.warningTime?.run {
                warningTimeConverter.fromString(
                    value = this,
                    warningUnit = WarningUnit.valueOf(model.warningUnit ?: WarningUnit.MINUTES.name)
                )
            } ?: 0,
            warningUnit = WarningUnit.valueOf(model.warningUnit ?: WarningUnit.MINUTES.name),
            color = model.color ?: ""
        )
    }

    override fun mapFromDomainModel(domainModel: Appointment): AppointmentListItemDto {
        val startDateTime = "${domainModel.date} ${domainModel.start}"
        val endDateTime = "${domainModel.date} ${domainModel.end}"

        return AppointmentListItemDto(
            appointmentId = domainModel.id,
            activityId = domainModel.activityId,
            title = domainModel.title,
            description = domainModel.description,
            start = startDateTime,
            end = endDateTime,
            planning = if (domainModel.planning) 1 else 0,
            accounted = if (domainModel.accounted) 1 else 0,
            invited = domainModel.invited,
            periodicity = domainModel.periodicity.serverFieldName,
            periodicityEnd = domainModel.periodicityEnd.toString(),
            memo = domainModel.memo,
            memoType = domainModel.memoType.name,
            warningTime = warningTimeConverter.toString(
                value = domainModel.warningTime,
                warningUnit = domainModel.warningUnit
            ),
            warningUnit = domainModel.warningUnit.name,
            color = null
        )
    }

    fun toDomainList(initial: List<AppointmentListItemDto>): List<Appointment> {
        return initial.mapNotNull { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Appointment>): List<AppointmentListItemDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}