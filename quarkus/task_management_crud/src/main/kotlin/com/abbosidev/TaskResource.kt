package com.abbosidev

import jakarta.persistence.EntityNotFoundException
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/api/v1/tasks")
@Produces(APPLICATION_JSON)
class TaskResource(private val taskService: TaskService) {

    @GET
    fun getAllTasks() = taskService.getAllTasks()

    @POST
    fun saveNewTask(task: TaskDto) = taskService.save(task)

    @GET
    @Path("/{id}")
    fun getTaskById(@PathParam("id") id: Long) =
        taskService.getTaskById(id) ?: throw EntityNotFoundException("Task with $id id does not exist")

    @PUT
    @Path("/{id}")
    fun updateTask(@PathParam("id") id: Long, task: TaskDto) {
        val updated = taskService.updateTask(id, task)
        if (updated < 1) {
            throw EntityNotFoundException("Task with $id id does not exist")
        }
    }

    @DELETE
    @Path("/{id}")
    fun deleteTask(@PathParam("id") id: Long) {
        val deleted = taskService.deleteTask(id)
        if (!deleted) {
            throw EntityNotFoundException("Task with $id id does not exist")
        }
    }
}