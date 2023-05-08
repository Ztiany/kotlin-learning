package me.ztiany.kotlin.kmm

import kotlinx.datetime.*

class Greeting {

    private val platform: Platform = getPlatform()

    fun greeting(): String {
        return "距离元旦还有${daysUntilNewYear()}天"
    }

    private fun daysUntilNewYear(): Int {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val closestNewYear = LocalDate(today.year + 1, 1, 1)
        return today.daysUntil(closestNewYear)
    }

}