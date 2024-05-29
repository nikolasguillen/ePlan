package com.cosmobile.eplan.time_stats.domain.use_cases

import android.content.SharedPreferences
import com.cosmobile.eplan.R
import com.cosmobile.eplan.core.data.model.ActivityDtoMapper
import com.cosmobile.eplan.core.data.model.InterventionDtoMapper
import com.cosmobile.eplan.core.data.services.ActivityService
import com.cosmobile.eplan.core.data.services.InterventionService
import com.cosmobile.eplan.core.domain.model.Activity
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.domain.model.Intervention
import com.cosmobile.eplan.core.util.UiText
import com.cosmobile.eplan.core.util.WORK_END_TIME
import com.cosmobile.eplan.core.util.WORK_START_TIME
import com.cosmobile.eplan.time_stats.domain.model.DayStats
import com.cosmobile.eplan.time_stats.ui.WeekStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.WeekFields

class GetInterventionsForMonth(
    private val interventionService: InterventionService,
    private val interventionDtoMapper: InterventionDtoMapper,
    private val activityService: ActivityService,
    private val activityDtoMapper: ActivityDtoMapper,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(
        token: String,
        month: Int,
        year: Int
    ): Flow<DataState<List<WeekStats>>> = flow {
        emit(DataState.loading())
        val startTime = sharedPreferences.getString(WORK_START_TIME, "09:00")?.split(":")?.let {
            LocalTime.of(it[0].toInt(), it[1].toInt())
        } ?: LocalTime.of(9, 0)
        val endTime = sharedPreferences.getString(WORK_END_TIME, "18:00")?.split(":")?.let {
            LocalTime.of(it[0].toInt(), it[1].toInt())
        } ?: LocalTime.of(18, 0)

        val activities = activityDtoMapper.toDomainList(activityService.getUserActivities(token))

        val timeStats = getInterventions(token = token, month = month, year = year)
            .mapToStats(
                activities = activities,
                startTime = startTime,
                endTime = endTime
            )
            .fillMissingDays()
            .mapToWeeks()

        delay(300)

        emit(DataState.success(timeStats))
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }

    private suspend fun getInterventions(
        token: String,
        month: Int,
        year: Int
    ): List<Intervention> {
        return interventionDtoMapper.toDomainList(
            interventionService.getPeriodInterventions(
                token = token,
                start = LocalDate.of(year, month, 1).toString(),
                end = LocalDate.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth())
                    .toString()
            )
        )
    }

    private fun List<Intervention>.mapToStats(
        activities: List<Activity>,
        startTime: LocalTime,
        endTime: LocalTime
    ): List<DayStats> {
        val minimumHours = (Duration.between(startTime, endTime).toMinutes() / 60.0) - 1.0

        return this.groupBy { it.date }
            .mapValues { (date, interventions) ->
                val regularHours = interventions
                    .filter {
                        (it.start.isAfter(startTime) || it.start == startTime)
                                && (it.end.isBefore(endTime) || it.end == endTime)
                    }
                    .filter {
                        (activities.find { activity -> activity.id == it.activityId }
                            ?.isRegular() == true)
                    }
                    .sumOf { Duration.between(it.start, it.end).toMinutes() / 60.0 }

                val diseaseHours = interventions
                    .filter {
                        (activities.find { activity -> activity.id == it.activityId }
                            ?.isDisease() == true)
                    }
                    .sumOf { Duration.between(it.start, it.end).toMinutes() / 60.0 }

                val vacationHours = interventions
                    .filter {
                        val activity = activities.find { activity -> activity.id == it.activityId }
                        activity?.isVacation() == true || activity?.isPermission() == true
                    }
                    .sumOf { Duration.between(it.start, it.end).toMinutes() / 60.0 }

                val overtime = interventions
                    .filter {
                        it.start.isBefore(
                            LocalTime.of(
                                9,
                                0
                            )
                        ) || it.end.isAfter(endTime)
                    }
                    .sumOf { Duration.between(it.start, it.end).toMinutes() / 60.0 }

                val isBelowMinimum = if (date.dayOfWeek.value > 5) {
                    false
                } else {
                    (regularHours + diseaseHours + vacationHours) < minimumHours
                }

                DayStats(
                    date = date,
                    standardTime = regularHours,
                    overtime = overtime,
                    vacation = vacationHours,
                    disease = diseaseHours,
                    isBelowMinimum = isBelowMinimum
                )
            }
            .values
            .toList()
            .sortedBy { it.date }
    }

    private fun List<DayStats>.fillMissingDays(): List<DayStats> {
        val firstDay = this.firstOrNull()?.date?.withDayOfMonth(1) ?: return this
        val lastDay =
            this.lastOrNull()?.date?.withDayOfMonth(this.last().date.lengthOfMonth())
                ?: return this

        val days = mutableListOf<DayStats>()
        var currentDay = firstDay
        while (currentDay != lastDay.plusDays(1)) {
            if (!this.any { it.date == currentDay }) {
                days.add(
                    DayStats(
                        date = currentDay,
                        standardTime = 0.0,
                        overtime = 0.0,
                        vacation = 0.0,
                        disease = 0.0,
                        isBelowMinimum = false
                    )
                )
            }
            currentDay = currentDay.plusDays(1)
        }
        days.addAll(this)
        return days.sortedBy { it.date }
    }

    private fun List<DayStats>.mapToWeeks(): List<WeekStats> {
        return this.groupBy { it.date.get(WeekFields.ISO.weekOfWeekBasedYear()) }
            .map { (weekNumber, days) ->
                WeekStats(
                    days = days,
                    weekNumber = weekNumber
                )
            }
    }
}