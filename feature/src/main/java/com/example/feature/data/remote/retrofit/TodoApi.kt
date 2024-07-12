package com.example.feature.data.remote.retrofit

import com.example.feature.data.remote.model.Request
import com.example.feature.data.remote.model.Task
import com.example.feature.data.remote.model.Tasks
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


/**
 * Interface defining methods to interact with the Todo API.
 */
interface TodoApiService {

    @GET("/todo/list")
    fun getTodoList(
        @Header("Authorization") token: String
    ): Call<Tasks>


    @POST("/todo/list")
    fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: Request<Task>,
        @Header("Authorization") token: String
    ): Call<Request<Tasks>>


    @PATCH("/todo/list")
    fun updateTodoList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: Response<Tasks>,
        @Header("Authorization") token: String
    ): Call<Tasks>


    @GET("/todo/list/{id}")
    fun getTodoItem(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<Task>


    @PUT("/todo/list/{id}")
    fun updateTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: Request<Task>,
        @Header("Authorization") token: String
    ): Call<Task>


    @DELETE("/todo/list/{id}")
    fun deleteTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Header("Authorization") token: String
    ): Call<Task>

}
