package com.cosmobile.eplan.core.domain.util

import com.cosmobile.eplan.core.domain.model.Gap
import com.cosmobile.eplan.core.domain.model.WorkActivity
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import kotlin.random.Random

fun List<WorkActivity>.calculateGaps(
    savedStartTime: LocalTime?,
    savedEndTime: LocalTime?,
    date: LocalDate? = null
): List<WorkActivity> {
    val listWithGaps = mutableListOf<WorkActivity>()
    val startTime = savedStartTime ?: LocalTime.of(9, 0)
    val endTime = savedEndTime ?: LocalTime.of(18, 0)

    if (this.isEmpty() && date != null) {
        listWithGaps.add(
            Gap(
                id = "gap-0-${Random.nextInt()}", // Random.nextInt() is used to avoid duplicate keys
                date = date,
                start = startTime,
                end = startTime.plusHours(1)
            )
        )
        return listWithGaps
    }

    this
        // Create Gap objects to fill the gaps between interventions
        .forEachIndexed { index, intervention ->
            if (index == 0) {
                if (Duration.between(
                        startTime,
                        intervention.start
                    ) > Duration.ZERO
                ) {
                    listWithGaps.add(
                        Gap(
                            id = "gap-0-${Random.nextInt()}", // Random.nextInt() is used to avoid duplicate keys
                            date = intervention.date,
                            start = startTime,
                            end = intervention.start
                        )
                    )
                }
            }
            listWithGaps.add(intervention)
            if (index < this.lastIndex) {
                val nextIntervention = this[index + 1]
                if (Duration.between(
                        intervention.end,
                        nextIntervention.start
                    ) > Duration.ZERO
                ) {
                    listWithGaps.add(
                        Gap(
                            id = "gap-$index-${Random.nextInt()}", // Random.nextInt() is used to avoid duplicate keys
                            date = intervention.date,
                            start = intervention.end,
                            end = nextIntervention.start,
                        )
                    )
                }
            } else {
                if (Duration.between(intervention.end, endTime) > Duration.ZERO) {
                    listWithGaps.add(
                        Gap(
                            id = "gap-$index-${Random.nextInt()}", // Random.nextInt() is used to avoid duplicate keys
                            date = intervention.date,
                            start = intervention.end,
                            end = endTime
                        )
                    )
                }
            }
        }

    listWithGaps.removeIf {
        it is Gap && it.start == LocalTime.of(
            13,
            0
        ) && it.end == LocalTime.of(14, 0)
    }

    return listWithGaps
}

fun List<WorkActivity>.removeGaps(): List<WorkActivity> {
    return this.filter { it !is Gap }
}