package com.lyf.contentprovider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentOpenHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    private val CREATE_SQL =
        "create table ${Students.TABLE_NAME}(_id integer primary key autoincrement, ${Students.Student().NAME}, ${Students.Student().AGE})"

    constructor(context: Context?, name: String?, version: Int) : this(context, name, null, version)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        println("onUpgrade oldVersion: $oldVersion newVersion: $newVersion")
    }
}