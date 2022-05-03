package com.example.eplan.network.model

import com.example.eplan.domain.model.Intervention
import com.example.eplan.domain.util.DomainMapper
import com.example.eplan.network.util.dateTimeParser
import com.example.eplan.network.util.removeHtmlBreak

class InterventionDtoMapper : DomainMapper<InterventionDto, Intervention> {

    override fun mapToDomainModel(model: InterventionDto): Intervention {
        val startDateTime = dateTimeParser(model.start)
        val endDateTime = dateTimeParser(model.end)

        return Intervention(
            activityId = model.idAttivita,
            id = model.id,
            title = model.title,
            description = removeHtmlBreak(model.description),
            date = startDateTime.first,
            start = startDateTime.second,
            end = endDateTime.second,
            movingTime = model.moveTime,
            km = model.moveDistance
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
            color = ""
        )
    }

    fun toDomainList(initial: List<InterventionDto>): List<Intervention> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Intervention>): List<InterventionDto> {
        return initial.map { mapFromDomainModel(it) }
    }


}