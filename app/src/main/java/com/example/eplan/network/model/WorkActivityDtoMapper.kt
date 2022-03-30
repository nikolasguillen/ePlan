package com.example.eplan.network.model

import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.domain.util.DomainMapper
import com.example.eplan.domain.util.durationCalculator
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WorkActivityDtoMapper : DomainMapper<WorkActivityDto, WorkActivity> {

    override fun mapToDomainModel(model: WorkActivityDto): WorkActivity {
        var startDateTime = dateTimeParser(model.start)
        var endDateTime = dateTimeParser(model.end)
        startDateTime = startDateTime.copy(first = dateFromNetwork(startDateTime.first))
        endDateTime = endDateTime.copy(first = dateFromNetwork(endDateTime.first))
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return WorkActivity(
            id = model.id,
            title = model.title,
            description = model.description,
            date = LocalDate.parse(startDateTime.first, formatter),
            start = startDateTime.second,
            end = endDateTime.second,
            movingTime = model.moveTime,
            km = model.moveDistance
        )
    }

    override fun mapFromDomainModel(domainModel: WorkActivity): WorkActivityDto {
        val startDateTime = dateToNetwork(domainModel.date) + " " + domainModel.start
        val endDateTime = dateToNetwork(domainModel.date) + " " + domainModel.end
        val formatter =  DateTimeFormatter.ofPattern("hh:mm:ss")
        val startTime = LocalTime.parse(domainModel.start, formatter)
        val endTime = LocalTime.parse(domainModel.end, formatter)


        return WorkActivityDto(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            start = startDateTime,
            end = endDateTime,
            duration = durationCalculator(start = startTime, end = endTime),
            moveTime = domainModel.movingTime,
            moveDistance = domainModel.km,
            color = "TODO"
        )
    }

    fun toDomainList(initial: List<WorkActivityDto>): List<WorkActivity> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<WorkActivity>): List<WorkActivityDto> {
        return initial.map { mapFromDomainModel(it) }
    }

    private fun dateTimeParser(dateTime: String): Pair<String, String> {
        return Pair(first = dateTime.split(" ")[0], second = dateTime.split(" ")[1].removeSuffix(":00"))
    }

    /* Converte la data dal formato yyyy-MM-dd a dd-MM-yyyy */
    private fun dateFromNetwork(date: String): String {
        val dayOfMonth = date.split("-")[2]
        val month = date.split("-")[1]
        val year = date.split("-")[0]

        return "$dayOfMonth-$month-$year"
    }

    /* Converte la data dal formato dd-MM-yyyy a yyyy-MM-dd */
    private fun dateToNetwork(date: LocalDate): String {
        val dayOfMonth = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        return "$year-$month-$dayOfMonth"
    }
}