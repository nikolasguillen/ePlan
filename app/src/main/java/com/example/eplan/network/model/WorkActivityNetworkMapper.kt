package com.example.eplan.network.model

import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.domain.util.EntityMapper
import com.example.eplan.domain.util.durationCalculator
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WorkActivityNetworkMapper : EntityMapper<WorkActivityNetworkEntity, WorkActivity> {

    override fun mapFromEntity(entity: WorkActivityNetworkEntity): WorkActivity {
        var startDateTime = dateTimeParser(entity.start)
        var endDateTime = dateTimeParser(entity.end)
        startDateTime = startDateTime.copy(first = dateFromNetwork(startDateTime.first))
        endDateTime = endDateTime.copy(first = dateFromNetwork(endDateTime.first))
        return WorkActivity(
            id = Integer.parseInt(entity.id),
            title = entity.title,
            description = entity.description,
            date = startDateTime.first,
            start = startDateTime.second,
            end = endDateTime.second,
            movingTime = "0",
            km = "0",
            close = false
        )
    }

    override fun mapToEntity(domainModel: WorkActivity): WorkActivityNetworkEntity {
        val startDateTime = dateToNetwork(domainModel.date) + " " + domainModel.start
        val endDateTime = dateToNetwork(domainModel.date) + " " + domainModel.end
        val formatter =  DateTimeFormatter.ofPattern("hh:mm:ss")
        val startTime = LocalTime.parse(domainModel.start, formatter)
        val endTime = LocalTime.parse(domainModel.end, formatter)


        return WorkActivityNetworkEntity(
            id = domainModel.id.toString(),
            title = domainModel.title,
            description = domainModel.description,
            start = startDateTime,
            end = endDateTime,
            duration = durationCalculator(start = startTime, end = endTime),
            color = "TODO"
        )
    }

    fun fromEntityList(initial: List<WorkActivityNetworkEntity>): List<WorkActivity> {
        return initial.map { mapFromEntity(it) }
    }

    fun toEntityList(initial: List<WorkActivity>): List<WorkActivityNetworkEntity> {
        return initial.map { mapToEntity(it) }
    }

    private fun dateTimeParser(dateTime: String): Pair<String, String> {
        return Pair(first = dateTime.split(" ")[0], second = dateTime.split(" ")[1])
    }

    /* Converte la data dal formato yyyy-MM-dd a dd-MM-yyyy */
    private fun dateFromNetwork(date: String): String {
        val dayOfMonth = date.split("-")[2]
        val month = date.split("-")[1]
        val year = date.split("-")[0]

        return "$dayOfMonth-$month-$year"
    }

    /* Converte la data dal formato dd-MM-yyyy a yyyy-MM-dd */
    private fun dateToNetwork(date: String): String {
        val dayOfMonth = date.split("-")[0]
        val month = date.split("-")[1]
        val year = date.split("-")[2]

        return "$year-$month-$dayOfMonth"
    }
}