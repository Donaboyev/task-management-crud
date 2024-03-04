package com.abbosidev

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDate

@Entity(name = "Task")
class TaskEntity : PanacheEntity() {

    companion object : PanacheCompanion<TaskEntity> {
        fun getAllTasks() = listAll()

        fun save(task: TaskDto) = persist(TaskEntity().apply {
            title = task.title
            type = task.type
            dueDate = task.dueDate
            description = task.description
        })

        fun updateTask(id: Long, task: TaskDto) = update(
            """
                title = ?1, 
                type = ?2, 
                dueDate = ?3, 
                description = ?4 
                WHERE id = ?5
            """.trimIndent(), task.title, task.type, task.dueDate, task.description, id
        )
    }

    lateinit var title: String
    lateinit var type: String

    @Column(name = "due_date")
    lateinit var dueDate: LocalDate
    lateinit var description: String
}
