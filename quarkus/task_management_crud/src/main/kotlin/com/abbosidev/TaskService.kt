package com.abbosidev

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class TaskService {
    fun getAllTasks() = TaskEntity.getAllTasks()

    @Transactional
    fun save(task: TaskDto) = TaskEntity.save(task)

    fun getTaskById(id: Long) = TaskEntity.findById(id)

    @Transactional
    fun updateTask(id: Long, task: TaskDto) = TaskEntity.updateTask(id, task)

    @Transactional
    fun deleteTask(id: Long) = TaskEntity.deleteById(id)
}