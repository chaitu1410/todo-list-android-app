package com.chaittnyashinde.mytodos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.todos_list_item.view.*

class TodoAdapter(val context: Context, val todos: ArrayList<Todo>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTodoText = view.tvTodoText
        val cbStatus = view.cbStatus
        val ivEditTodo = view.ivEditTodo
        val ivDeleteTodo = view.ivDeleteTodo
        val todoItem = view.todoItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(context)
                .inflate(
                    R.layout.todos_list_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todos[position]

        holder.tvTodoText.text = todo.text
        holder.cbStatus.isChecked = todo.status

        if(todo.status){
            holder.todoItem.setBackgroundResource(R.drawable.completed_todo_background)
        }
        else{
            holder.todoItem.setBackgroundResource(R.drawable.todos_list_item_background)
        }

        holder.ivEditTodo.setOnClickListener {
            if (context is MainActivity){
                context.updateTodo(todo)
            }
        }

        holder.ivDeleteTodo.setOnClickListener {
            if (context is MainActivity){
                context.deleteTodo(todo)
            }
        }

        holder.cbStatus.setOnClickListener {
            if(context is MainActivity){
                context.markCompleted(Todo(todo.id, todo.text, !todo.status))
            }
        }

    }


    override fun getItemCount(): Int {
        return todos.size
    }

}