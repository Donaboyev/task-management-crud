package com.abbosidev

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.common.mapper.TypeRef
import jakarta.ws.rs.core.MediaType.APPLICATION_JSON
import org.jboss.resteasy.reactive.RestResponse.StatusCode.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import java.time.LocalDate

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class TaskResourceTest {

    private val tempTaskTitle = "TEMP_TASK_TITLE"
    private val tempTaskDescription = "TEMP_TASK_DESCRIPTION"
    private val tempTaskType = "TEMP_TASK_TYPE"
    private val tempTaskDueDate = LocalDate.now()

    private val updateTaskTitle = "UPDATE_TASK_TITLE"
    private val updateTaskDescription = "UPDATE_TASK_DESCRIPTION"
    private val updateTaskType = "UPDATE_TASK_TYPE"
    private val updateTaskDueDate = tempTaskDueDate.plusDays(1)

    companion object {
        var createdTaskId: Long = 0
    }

    @Test
    fun `test get all tasks rest http response`() {
        given()
            .`when`()
            .get("/api/v1/tasks")
            .then()
            .statusCode(200)
            .contentType(APPLICATION_JSON)
    }

    @Test
    @Order(1)
    fun `test saving new Task`() {
        val tasksBefore: List<TaskEntity> = given()
            .`when`()
            .get("/api/v1/tasks")
            .then()
            .statusCode(OK)
            .contentType(APPLICATION_JSON)
            .extract()
            .`as`(getListOfTaskEntityTypeRef())

        val newTask = TaskDto(
            title = tempTaskTitle,
            type = tempTaskType,
            description = tempTaskDescription,
            dueDate = tempTaskDueDate
        )
        val response = given()
            .contentType(APPLICATION_JSON)
            .body(newTask)
            .`when`()
            .post("/api/v1/tasks")
            .then()
            .statusCode(OK)
//            .statusCode(NO_CONTENT)  // fails
//            .contentType(TEXT_PLAIN) // fails
            .contentType(APPLICATION_JSON)
            .extract()
//            .`as`(TaskDto::class.java) // fails
            .`as`(TaskEntity::class.java)
        createdTaskId = response.id ?: 0

        assertNotNull(response.id)
        assertEquals(response.title, tempTaskTitle)
        assertEquals(response.description, tempTaskDescription)
        assertEquals(response.dueDate, tempTaskDueDate)
//        assertEquals(response.type, tempTaskDescription) // fails
        assertEquals(response.type, tempTaskType)

        val tasksAfter: List<TaskEntity> = given()
            .`when`()
            .get("/api/v1/tasks")
            .then()
            .statusCode(OK)
            .contentType(APPLICATION_JSON)
            .extract()
            .`as`(getListOfTaskEntityTypeRef())

//        assertEquals(tasksBefore.size, tasksAfter.size) // fails
        assertEquals(tasksBefore.size + 1, tasksAfter.size)
    }

    @Test
    @Order(2)
    fun `test getting latest saved task`() {
        val task = given()
            .pathParams("id", createdTaskId)
            .`when`()
            .get("/api/v1/tasks/{id}")
            .then()
            .statusCode(OK)
            .contentType(APPLICATION_JSON)
            .extract()
            .`as`(getTaskEntityTypeRef())

        assertNotNull(task.id)
        assertEquals(task.title, tempTaskTitle)
//        assertEquals(task.description, tempTaskTitle) // fails
        assertEquals(task.description, tempTaskDescription)
        assertEquals(task.type, tempTaskType)
        assertEquals(task.dueDate, tempTaskDueDate)
    }

    @Test
    @Order(3)
    fun `test getting an exception when try to get not existed task`() {
        given()
            .pathParams("id", 0)
            .`when`()
            .get("/api/v1/tasks/{id}")
            .then()
//            .statusCode(OK) // fails
            .statusCode(INTERNAL_SERVER_ERROR)
    }

    @Test
    @Order(4)
    fun `test updating latest task`() {
        val updated = TaskDto(
            title = updateTaskTitle,
            type = updateTaskType,
            description = updateTaskDescription,
            dueDate = updateTaskDueDate
        )
        val response = given()
            .contentType(APPLICATION_JSON)
            .pathParams("id", createdTaskId)
            .body(updated)
            .`when`()
            .put("/api/v1/tasks/{id}")
            .then()
            .statusCode(OK)
            .contentType(APPLICATION_JSON)
            .extract()
            .`as`(getTaskEntityTypeRef())

        assertNotNull(response.id)
        assertEquals(response.title, updateTaskTitle)
        assertEquals(response.description, updateTaskDescription)
        assertEquals(response.type, updateTaskType)
        assertEquals(response.dueDate, updateTaskDueDate)


        val task = given()
            .pathParams("id", createdTaskId)
            .`when`()
            .get("/api/v1/tasks/{id}")
            .then()
            .statusCode(OK)
            .contentType(APPLICATION_JSON)
            .extract()
            .`as`(getTaskEntityTypeRef())

        assertNotNull(task.id)
        assertEquals(task.title, updateTaskTitle)
        assertEquals(task.description, updateTaskDescription)
        assertEquals(task.type, updateTaskType)
        assertEquals(task.dueDate, updateTaskDueDate)
    }

    @Test
    @Order(5)
    fun `test deleting latest saved task`() {
        val tasksBefore: List<TaskEntity> = given()
            .`when`()
            .get("/api/v1/tasks")
            .then()
            .statusCode(OK)
            .contentType(APPLICATION_JSON)
            .extract()
            .`as`(getListOfTaskEntityTypeRef())

        given()
            .pathParams("id", createdTaskId)
            .`when`()
            .delete("/api/v1/tasks/{id}")
            .then()
            .statusCode(NO_CONTENT)

        given()
            .pathParams("id", createdTaskId)
            .`when`()
            .get("/api/v1/tasks/{id}")
            .then()
            .statusCode(INTERNAL_SERVER_ERROR)

        val tasksAfter: List<TaskEntity> = given()
            .`when`()
            .get("/api/v1/tasks")
            .then()
            .statusCode(OK)
            .contentType(APPLICATION_JSON)
            .extract()
            .`as`(getListOfTaskEntityTypeRef())

//        assertEquals(tasksBefore.size, tasksAfter.size) // fails
        assertEquals(tasksBefore.size - 1, tasksAfter.size)
    }

    private fun getListOfTaskEntityTypeRef() = object : TypeRef<List<TaskEntity>>() {}

    private fun getTaskEntityTypeRef() = object : TypeRef<TaskEntity>() {}
}