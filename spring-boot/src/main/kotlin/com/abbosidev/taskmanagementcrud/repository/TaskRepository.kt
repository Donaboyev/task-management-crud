package com.abbosidev.taskmanagementcrud.repository

import com.abbosidev.taskmanagementcrud.model.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TaskRepository : JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM Task ORDER BY due_date desc", nativeQuery = true)
    fun getAllTasksDueDateDescending(): List<Task>
}