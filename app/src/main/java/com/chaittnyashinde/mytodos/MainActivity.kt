package com.chaittnyashinde.mytodos

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_todo.*
import kotlinx.android.synthetic.main.dialog_update_todo.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener { view ->
            addTodo()
        }
        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        val handler = DatabaseHandler(this)

        if(handler.getAllTodos().size > 0){
            rvTodos.visibility = View.VISIBLE
            tvNoTodo.visibility = View.GONE
            val adapter = TodoAdapter(this, handler.getAllTodos())

            rvTodos.layoutManager = LinearLayoutManager(this)
            rvTodos.adapter = adapter
        }else{
            rvTodos.visibility = View.GONE
            tvNoTodo.visibility = View.VISIBLE
        }
    }

    private fun addTodo() {
        val addDialog = Dialog(this, R.style.ThemeDialog)
        addDialog.setCancelable(false)

        addDialog.setContentView(R.layout.dialog_add_todo)

        addDialog.btnAdd.setOnClickListener {
            val text = addDialog.etAddTodo.text.toString()
            val handler = DatabaseHandler(this)
            if(text.isNotEmpty()){
                val status = handler.addTodo(Todo(0, text, false))
                if(status > -1){
                    Toast.makeText(applicationContext, "Todo Added", Toast.LENGTH_SHORT).show()
                    setupRecyclerview()
                    addDialog.dismiss()
                }
            }else{
                Toast.makeText(applicationContext, "Todo text is required", Toast.LENGTH_SHORT).show()
            }
        }
        addDialog.btnCancel.setOnClickListener {
            addDialog.dismiss()
        }

        addDialog.show()
    }

    fun updateTodo(todo: Todo){
        val updateDialog = Dialog(this, R.style.ThemeDialog)
        updateDialog.setCancelable(false)

        updateDialog.setContentView(R.layout.dialog_update_todo)
        updateDialog.etUpdateTodo.setText(todo.text)

        updateDialog.btnUpdate.setOnClickListener{
            val text = updateDialog.etUpdateTodo.text.toString()
            val handler = DatabaseHandler(this)

            if (text.isNotEmpty()) {
                val status = handler.updateTodo(Todo(todo.id, text, todo.status))
                if (status > -1){
                    Toast.makeText(applicationContext, "Todo Updated", Toast.LENGTH_SHORT).show()
                    setupRecyclerview()
                    updateDialog.dismiss()
                }
            }
            else{
                Toast.makeText(applicationContext, "Todo text can not empty", Toast.LENGTH_SHORT).show()
            }
        }

        updateDialog.btnUpdateCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    fun markCompleted(todo: Todo){
        val handler = DatabaseHandler(this)
        val status = handler.updateTodo(Todo(todo.id, todo.text, todo.status))
        setupRecyclerview()
    }

    fun deleteTodo(todo: Todo){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure to delete todo?")
        builder.setIcon(R.drawable.ic_alert)

        builder.setPositiveButton("Yes"){ dialogInterface, which ->
            val handler = DatabaseHandler(this)
            val status = handler.deleteTodo(todo)
            if(status > -1){
                Toast.makeText(applicationContext, "Todo deleted", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
                setupRecyclerview()
            }
        }
        builder.setNegativeButton("No"){dialogInerface, which ->
            dialogInerface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}