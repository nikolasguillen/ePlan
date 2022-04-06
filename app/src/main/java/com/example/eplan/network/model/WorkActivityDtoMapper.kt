package com.example.eplan.network.model

import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.domain.util.DomainMapper
import com.example.eplan.domain.util.durationCalculator
import java.time.LocalDate
import java.time.LocalTime

class WorkActivityDtoMapper : DomainMapper<WorkActivityDto, WorkActivity> {

    override fun mapToDomainModel(model: WorkActivityDto): WorkActivity {
        val startDateTime = dateTimeParser(model.start)
        val endDateTime = dateTimeParser(model.end)

        return WorkActivity(
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
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            start = startDateTime,
            end = endDateTime,
            duration = durationCalculator(start = domainModel.start, end = domainModel.end),
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

    private fun dateTimeParser(dateTime: String): Pair<LocalDate, LocalTime> {
        return Pair(
            first = dateFromNetwork(dateTime.split(" ")[0]),
            second = LocalTime.parse(dateTime.split(" ")[1])
        )
    }

    /* Converte la data dal formato yyyy-MM-dd a dd-MM-yyyy */
    private fun dateFromNetwork(date: String): LocalDate {
        val dayOfMonth = date.split("-")[2].toInt()
        val month = date.split("-")[1].toInt()
        val year = date.split("-")[0].toInt()

        return LocalDate.of(year, month, dayOfMonth)
    }

    /* Converte la data dal formato dd-MM-yyyy a yyyy-MM-dd */
    private fun dateToNetwork(date: LocalDate): String {
        val dayOfMonth = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        return "$year-$month-$dayOfMonth"
    }
}