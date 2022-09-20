package com.example.tugasbesar

import com.example.tugasbesar.entity.eventTokyo

object TaskListEvent {
    val taskListEvent = listOf<TaskEventTokyo>(
        TaskEventTokyo("Earth Day Tokyo 2022", "Yoyogi Park", "16th-17th April 2022"),
        TaskEventTokyo("Kanagawa Shimbun Fireworks Festival", "Minato Mirai area", "early in August"),
        TaskEventTokyo("2022 Tokyo Game Show","Chiba’s Makuhari Messe","15-18 September 2022")
    )
}