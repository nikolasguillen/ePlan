package com.cosmobile.eplan.core.data.model

import com.cosmobile.eplan.core.data.dateTimeParser
import com.cosmobile.eplan.core.data.removeHtmlBreak
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.domain.util.DomainMapper

class InterventionDtoMapper : DomainMapper<InterventionDto, Intervention> {

    override fun mapToDomainModel(model: InterventionDto): Intervention? {
        if (model.idAttivita == null || model.id == null || model.title == null || model.start == null || model.end == null) {
            return null
        }
        val startDateTime = dateTimeParser(model.start)
        val endDateTime = dateTimeParser(model.end)

        return Intervention(
            activityId = model.idAttivita,
            id = model.id,
            title = model.title,
            description = removeHtmlBreak(model.description ?: ""),
            date = startDateTime.first,
            start = startDateTime.second,
            end = endDateTime.second,
            movingTime = model.moveTime ?: "",
            km = model.moveDistance ?: "",
            color = model.color ?: ""
        )
    }

    override fun mapFromDomainModel(domainModel: Intervention): InterventionDto {
        val startDateTime = "${domainModel.date} ${domainModel.start}"
        val endDateTime = "${domainModel.date} ${domainModel.end}"

        return InterventionDto(
            idAttivita = domainModel.activityId,
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            start = startDateTime,
            end = endDateTime,
            moveTime = domainModel.movingTime,
            moveDistance = domainModel.km,
            color = domainModel.color
        )
    }

    fun toDomainList(initial: List<InterventionDto>): List<Intervention> {
        return initial.mapNotNull { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Intervention>): List<InterventionDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}