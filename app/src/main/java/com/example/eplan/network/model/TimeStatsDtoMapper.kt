package com.example.eplan.network.model

import com.example.eplan.domain.model.TimeStats
import com.example.eplan.domain.util.DomainMapper
import java.time.LocalDate

class TimeStatsDtoMapper: DomainMapper<TimeStatsDto, TimeStats> {
    override fun mapToDomainModel(model: TimeStatsDto): TimeStats {
        return TimeStats(
            date = LocalDate.parse(model.date),
            standardTime = model.standardTime,
            overtime = model.overtime,
            vacation = model.vacation,
            disease = model.disease
        )
    }

    override fun mapFromDomainModel(domainModel: TimeStats): TimeStatsDto {
        return TimeStatsDto(
            date = domainModel.date.toString(),
            standardTime = domainModel.standardTime,
            overtime = domainModel.overtime,
            vacation = domainModel.vacation,
            disease = domainModel.disease
        )
    }

    fun toDomainList(initial: List<TimeStatsDto>): List<TimeStats> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<TimeStats>): List<TimeStatsDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}