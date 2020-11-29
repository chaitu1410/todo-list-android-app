package com.chaittnyashinde.mytodos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * @Author : Chaitanya Shinde
 * */
class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TodosDB"
        private const val TABLE_TODOS = "todos"

        private const val KEY_ID = "_id"
        private const val KEY_TEXT = "text"
        private const val KEY_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TODOS_TABLE = (
            "CREATE TABLE $TABLE_TODOS($KEY_ID INTEGER PRIMARY KEY, $KEY_TEXT TEXT, $KEY_STATUS BOOLEAN)"
        )
        db?.execSQL(CREATE_TODOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TODOS")
        onCreate(db)
    }


    /**
     * Function for CRUD functionality of Todos
     * */

    fun addTodo(todo: Todo): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()

        contentValues.put(KEY_TEXT, todo.text)
        contentValues.put(KEY_STATUS, todo.status)

        val success = db.insert(TABLE_TODOS, null, contentValues)
        db.close()

        return success
    }

    fun getAllTodos(): ArrayList<Todo>{
        val allTodos: ArrayList<Todo> = ArrayList<Todo>()
        val selectQuery = "SELECT * FROM $TABLE_TODOS"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery,null)
        }
        catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var text: String
        var status: Boolean

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                text = cursor.getString(cursor.getColumnIndex(KEY_TEXT))
                status = cursor.getInt(cursor.getColumnIndex(KEY_STATUS)) == 1

                val todo = Todo(id = id, text = text, status = status)
                allTodos.add(todo)

            }while(cursor.moveToNext())
        }

        db.close()
        return allTodos
    }

    fun updateTodo(todo: Todo): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_TEXT, todo.text)
        contentValues.put(KEY_STATUS, todo.status)

        val success = db.update(TABLE_TODOS, contentValues, "$KEY_ID = ${todo.id}", null)

        db.close()
        return success
    }

    fun deleteTodo(todo: Todo): Int{
        val db = this.writableDatabase

        val success = db.delete(TABLE_TODOS, "$KEY_ID = ${todo.id}", null)

        db.close()
        return success
    }

}