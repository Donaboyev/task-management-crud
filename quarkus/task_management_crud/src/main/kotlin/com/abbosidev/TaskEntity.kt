package com.abbosidev

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.text.DateFormat
import java.time.Instant
import java.util.Date

@Entity(name = "Task")
class TaskEntity : PanacheEntity() {

    companion object : PanacheCompanion<TaskEntity> {
        fun getAllTasks() = listAll()

        fun save(task: TaskDto) = persist(
            TaskEntity().apply {
                title = task.title
                type = task.type
                dueDate = task.dueDate
                description = task.description
            }
        )

        fun updateTask(id: Long, task: TaskDto) = update(
            "title = '${task.title}', " +
                    "type = '${task.type}', " +
                    "due_date = ${task.dueDate}, " +
                    "description = '${task.description}' " +
                    "WHERE id = $id"
        )
    }

    lateinit var title: String
    lateinit var type: String

    @Column(name = "due_date")
    lateinit var dueDate: Date
    lateinit var description: String
}
