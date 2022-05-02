package com.example.eplan.network.model

import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.domain.util.DomainMapper
import com.example.eplan.network.util.dateTimeParser
import com.example.eplan.network.util.dateToNetwork
import java.time.LocalDate
import java.time.LocalTime

class WorkActivityDtoMapper : DomainMapper<WorkActivityDto, WorkActivity> {

    override fun mapToDomainModel(model: WorkActivityDto): WorkActivity {
        val startDateTime = dateTimeParser(model.start)
        val endDateTime = dateTimeParser(model.end)

        return WorkActivity(
            activityId = model.idAttivita,
            id = model.id,
            title = model.title,
            description = model.description,
            date = startDateTime.first,
            start = startDateTime.second,
            end = endDateTime.second,
            movingTime = model.moveTime,
            km = model.moveDistance
        )
    }

    override fun mapFromDomainModel(domainModel: WorkActivity): WorkActivityDto {
        val startDateTime = dateToNetwork(domainModel.date) + " " + domainModel.start
        val endDateTime = dateToNetwork(domainModel.date) + " " + domainModel.end

        return WorkActivityDto(
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

    fun toDomainList(initial: List<WorkActivityDto>): List<WorkActivity> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<WorkActivity>): List<WorkActivityDto> {
        return initial.map { mapFromDomainModel(it) }
    }


}