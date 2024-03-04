package com.abbosidev.taskmanagementcrud.controller

import com.abbosidev.taskmanagementcrud.model.Task
import com.abbosidev.taskmanagementcrud.service.TaskService
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tasks")
@CrossOrigin("*")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getAllTasks() = taskService.getTasks()

    @PostMapping
    fun addTask(@RequestBody task: Task) = taskService.save(task)

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): Task =
        taskService.getTaskById(id).orElseThrow {
            EntityNotFoundException("Requested task not found")
        }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable("id") id: Long, @RequestBody task: Task): ResponseEntity<*> {
        if (taskService.existsById(id)) {
            val currentTask = taskService.getTaskById(id).orElseThrow {
                EntityNotFoundException("Requested task not found")
            }
            currentTask.title = task.title
            currentTask.dueDate = task.dueDate
            currentTask.type = task.type
            currentTask.description = task.description
            taskService.save(currentTask)
            return ResponseEntity.ok(task)
        } else {
            val message = HashMap<String, String>().apply {
                put("message", "$id task not found or matched")
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<*> {
        if (taskService.existsById(id)) {
            taskService.delete(id)
            val message = HashMap<String, String>().apply {
                put("message", "$id task removed")
            }
            return ResponseEntity.ok(message)
        } else {
            val message = HashMap<String, String>().apply {
                put("message", "$id task not found or matched")
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message)
        }
    }


}