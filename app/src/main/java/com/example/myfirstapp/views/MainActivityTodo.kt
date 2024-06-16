package com.example.myfirstapp.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.model.TodoModel
import com.example.myfirstapp.network.RetrofitClient
import com.example.myfirstapp.network.TodoServices
import com.example.myfirstapp.network.TodosRepository
import com.example.myfirstapp.viewmodels.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivityTodo: AppCompatActivity(), TodoOnClickLListener {

    // Views
    private lateinit var todoListRecyclerView: RecyclerView
    private lateinit var redoRequest: FloatingActionButton


    companion object {
        val TODO_MODEL_EXTRA = "TODO_MODEL_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        this.observeTodoListData()
        this.setButton()
    }

    private fun setButton(){
        this.redoRequest = findViewById(R.id.redo_request)
        this.redoRequest.setOnClickListener {
            this.fetchTodoList()
        }
    }

    private fun setUpActivityViews(data: List<TodoModel>) {
        this.todoListRecyclerView = findViewById(R.id.todo_list_recycler_view)

        // Setup RV Adapter
        val todoAdapter = TodoListAdapter(data, this)

        // Setup Linear layout manager
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation  = LinearLayoutManager.VERTICAL

        this.todoListRecyclerView.layoutManager = linearLayoutManager
        this.todoListRecyclerView.setAdapter(todoAdapter)
    }


    // Data fetch and observing
    private fun fetchTodoList() {
        // viewModel.getTodo()
        TodoViewModel.fetchTodoFromRepo()
    }


    private fun observeTodoListData() {
        TodoViewModel.todos.observe(this) { todoList ->
            this.setUpActivityViews(todoList)
        }
    }



    override fun displayTodoDetail(todo: TodoModel, position: Int) {
        Intent(this, TodoDetailActivity::class.java).also {
            it.putExtra(TODO_MODEL_EXTRA, todo)
            it.putExtra("position", position)
            startActivity(it)
        }
    }

    // A function that starts new activity with the selected todomodel
}

interface TodoOnClickLListener {
    fun displayTodoDetail(todo: TodoModel, position: Int)
}

fun verifyPassword(motDePasse: String): List<String> {
    val erreurs = mutableListOf<String>()

    // Vérifier la longueur minimale
    if (motDePasse.length < 6) {
        erreurs.add("Le mot de passe doit contenir au moins 6 caractères.")
    }

    // Vérifier au moins une lettre majuscule
    if (!Regex("[A-Z]").containsMatchIn(motDePasse)) {
        erreurs.add("Le mot de passe doit contenir au moins une lettre majuscule.")
    }

    // Vérifier au moins une lettre minuscule
    if (!Regex("[a-z]").containsMatchIn(motDePasse)) {
        erreurs.add("Le mot de passe doit contenir au moins une lettre minuscule.")
    }

    // Vérifier au moins un chiffre
    if (!Regex("\\d").containsMatchIn(motDePasse)) {
        erreurs.add("Le mot de passe doit contenir au moins un chiffre.")
    }

    // Vérifier au moins un caractère spécial
    val caracteresSpeciaux = "~`!@#\$%\\^&*\\(\\)-_+=<>?/\\[]\\{}|"

    val pattern: Pattern = Pattern.compile(caracteresSpeciaux)
    val matcher: Matcher = pattern.matcher(motDePasse)
    val passwordMatchesqPattern = matcher.matches()
    if (passwordMatchesqPattern) {
        erreurs.add("Le mot de passe doit contenir au moins un caractère spécial parmi $caracteresSpeciaux.")
    }

    return erreurs
}