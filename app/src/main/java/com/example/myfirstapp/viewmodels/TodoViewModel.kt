package com.example.myfirstapp.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myfirstapp.model.TodoDto
import com.example.myfirstapp.model.TodoModel
import com.example.myfirstapp.network.RetrofitClient
import com.example.myfirstapp.network.TodosRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

object TodoViewModel{
    var todos: MutableLiveData<ArrayList<TodoModel>> = MutableLiveData()

    fun fetchTodoFromRepo() {
        val todosApiResponse = TodosRepository(
            RetrofitClient.createTodoService()).fetchTodos()

        todosApiResponse.enqueue(object : Callback<List<TodoDto>> {
            override fun onFailure(p0: Call<List<TodoDto>>, t: Throwable) {

            }

            override fun onResponse(p0: Call<List<TodoDto>>, response: Response<List<TodoDto>>) {
                val responseBody: List<TodoDto> = response.body() ?: listOf()
                val mappedResponse = responseBody.map {
                    TodoModel(
                        it.id,
                        it.title,
                        it.description,
                        it.due_date,
                        it.completed
                    )
                }
                todos.value = ArrayList(mappedResponse)
            }
        })
    }
}