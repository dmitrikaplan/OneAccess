package ru.kaplaan.kcloak.domain

import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.SelectLimitPercentAfterOffsetStep


fun <R: Record> SelectConditionStep<R>.paging(pageNumber: Int, pageSize: Int): SelectLimitPercentAfterOffsetStep<R> {
    val from = (pageNumber - 1) * pageSize
    return this.offset(from)
        .limit(pageSize)
}