package com.example.myfirstapp.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myfirstapp.R
import com.example.myfirstapp.model.TodoModel
import com.example.myfirstapp.viewmodels.TodoViewModel
import com.example.myfirstapp.views.MainActivityTodo.Companion.TODO_MODEL_EXTRA
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoDetailActivity : AppCompatActivity() {

    private lateinit var todoTitleTextView: TextView
    private lateinit var todoDateTextView: TextView
    private lateinit var todoDescriptionTextView: EditText

    private lateinit var deleteImageView: ImageView
    private lateinit var updateImageView: ImageView
    private lateinit var validateTodoButton: Button
    private lateinit var hideKeyboard: FloatingActionButton

    // Data from intent
    private var todoDetailIsChecked: Boolean = false
    private var todoDetailId:Int = 0
    private lateinit var todoDetailTitle: String
    private lateinit var todoDetailDesc: String
    private lateinit var todoDetailDate: String
    private var position: Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        this.getIntentExtraData()

        if(this.todoDetailIsChecked){
            val markAsChecked:Button = findViewById(R.id.validate_todo_button)
            markAsChecked.text = "Mark as Unchecked"
        }

        this.setupViews()
        this.handleButtonObservation()
    }


    private fun getIntentExtraData() {
        if (this.intent.hasExtra(TODO_MODEL_EXTRA)){

            val todoData = intent.getParcelableExtra<TodoModel>(TODO_MODEL_EXTRA)!!
            this.todoDetailTitle = todoData.title ?: ""
            this.todoDetailDesc = todoData.description ?: ""
            this.todoDetailDate = todoData.date ?: ""
            this.todoDetailIsChecked = todoData.isChecked
            this.todoDetailId = todoData.id ?: 0
        }
        if(this.intent.hasExtra("position")){
            this.position = intent.getIntExtra("position", -1)
        }
    }
    private fun setupViews() {
        this.todoTitleTextView = findViewById(R.id.todo_detail_title_text_view)
        this.todoTitleTextView.text = this.todoDetailTitle

        this.todoDateTextView = findViewById(R.id.todo_detail_date_text_view)
        this.todoDateTextView.text = this.todoDetailDate

        this.todoDescriptionTextView = findViewById(R.id.todo_detail_description_text_view)
        this.todoDescriptionTextView.setText(this.todoDetailDesc)

        this.deleteImageView = findViewById(R.id.delete_todo_image_view)
        this.updateImageView = findViewById(R.id.edit_todo_image_view)
        this.validateTodoButton = findViewById(R.id.validate_todo_button)
        this.hideKeyboard = findViewById(R.id.hide_keyboard)
    }

    private fun handleButtonObservation() {
        this.handleTodoDeleting()
        this.handleTodoEditing()
        this.handleTodoValidating()
        this.handleKeyboardHiding()
    }

    private fun handleKeyboardHiding(){
        this.hideKeyboard.setOnClickListener {
            val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(todoDescriptionTextView.windowToken, 0)
        }
    }

    private fun handleTodoDeleting(){
        this.deleteImageView.setOnClickListener {
            TodoViewModel.todos.value?.removeAt(this.position)
            val intent = Intent(this, MainActivityTodo::class.java)
            startActivity(intent)
        }
    }

    private fun handleTodoEditing(){
        this.updateImageView.setOnClickListener {
            TodoViewModel.todos.value?.get(position)?.description = this.todoDescriptionTextView.text.toString()
            val intent = Intent(this, MainActivityTodo::class.java)
            startActivity(intent)
        }
    }

    private fun handleTodoValidating(){
        this.validateTodoButton.setOnClickListener {
            TodoViewModel.todos.value?.get(position)?.isChecked = !TodoViewModel.todos.value?.get(position)?.isChecked!!
            val intent = Intent(this, MainActivityTodo::class.java)
            startActivity(intent)
        }
    }
}