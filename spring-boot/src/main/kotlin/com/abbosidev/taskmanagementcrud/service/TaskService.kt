package com.abbosidev.taskmanagementcrud.service

import com.abbosidev.taskmanagementcrud.model.Task
import com.abbosidev.taskmanagementcrud.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(private val taskRepository: TaskRepository) {

    @Transactional(readOnly = true)
    fun getTasks() = taskRepository.getAllTasksDueDateDescending()

    @Transactional
    fun save(task: Task) = taskRepository.saveAndFlush(task)

    @Transactional(readOnly = true)
    fun existsById(id: Long) = taskRepository.existsById(id)

    @Transactional(readOnly = true)
    fun getTaskById(id: Long) = taskRepository.findById(id)

    fun delete(id: Long) = taskRepository.deleteById(id)

}