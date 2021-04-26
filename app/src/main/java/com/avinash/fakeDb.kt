package com.avinash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class Task(
        val taskId: Int,
        val taskName: String,
        val taskDescription: String,
        val taskComments: List<String>,
        val subTasks: List<Int>
)


val theTaskList = List(20) {
    Task(
            taskId = it, taskName = "Task Name $it",
            taskDescription = "asdas $it dasf $it asd",
            taskComments = (0..it).map {
                "Comment $it"
            },
            subTasks = (20 - 1 downTo it + 1).map { it }.sorted()
    )
}


val tasksListLiveData = MutableLiveData(theTaskList)


data class Bug(
        val bugId: Int,
        val bugName: String,
        val bugDescription: String,
        val linkedTasks: List<Int>
)

val theBugList = List(20) {
    Bug(
            bugId = it, bugName = "Bug Name $it",
            bugDescription = "asdas $it dasf $it asd",
            linkedTasks = (20 - 1 downTo it + 1).map { it }.sorted()
    )
}

val bugListLiveData = MutableLiveData(theBugList)