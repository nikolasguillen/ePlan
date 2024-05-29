package com.cosmobile.eplan.core.data.model

import com.cosmobile.eplan.core.domain.model.Activity
import com.cosmobile.eplan.core.domain.util.DomainMapper

class ActivityDtoMapper : DomainMapper<ActivityDto, Activity> {
    override fun mapToDomainModel(model: ActivityDto): Activity {
        return Activity(
            id = model.activityId,
            name = model.activityName
        )
    }

    override fun mapFromDomainModel(domainModel: Activity): ActivityDto {
        return ActivityDto(
            activityId = domainModel.id,
            activityName = domainModel.name
        )
    }

    fun toDomainList(initial: List<ActivityDto>): List<Activity> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Activity>): List<ActivityDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}