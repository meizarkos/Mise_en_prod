package com.example.myfirstapp.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.R
import com.example.myfirstapp.model.TodoModel

class TodoListAdapter(val todoList: List<TodoModel>, private val todoClickHandler: TodoOnClickLListener): RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val todoView = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_cell_layout, parent, false)
        return TodoViewHolder(todoView)
    }

    override fun getItemCount(): Int {
        return this.todoList.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodoData = this.todoList[position] // Get the data at the right position
        holder.bind(currentTodoData)
        holder.itemView.setOnClickListener {
            //Toast.makeText(holder.itemView.context, "Selected note n°${position + 1}", Toast.LENGTH_LONG).show()
            todoClickHandler.displayTodoDetail(currentTodoData, position)
        }
    }


    // Class representing the object linked to the XML
    // of on cell of the RV
    inner class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var todoTitle: TextView
        var todoDate: TextView
        var todoCheckBox: CheckBox

        init {
            this.todoTitle = itemView.findViewById(R.id.todo_title_text_view)
            this.todoDate = itemView.findViewById(R.id.todo_date_textview)
            this.todoCheckBox = itemView.findViewById(R.id.todo_check_state_check_box)
        }

        fun bind(todoModel: TodoModel) {
            this.todoTitle.text = todoModel.title
            this.todoDate.text = todoModel.date
            this.todoCheckBox.isChecked = todoModel.isChecked
        }
    }
}