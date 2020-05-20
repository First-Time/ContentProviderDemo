package com.lyf.contentprovider

import android.content.ContentUris
import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_content_resolver.*

class ContentResolverActivity : AppCompatActivity() {

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: BasicAdapter
    private var dataList: MutableList<InfoModel> = mutableListOf()
    private lateinit var studentObserver: ContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_resolver)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        mAdapter = BasicAdapter(dataList)
        recyclerView.adapter = mAdapter

        initListener()

        studentObserver = StudentObserver(Handler())

        contentResolver.registerContentObserver(
            Students.Student().CONTENT_URI_STUDENTS,
            true,
            studentObserver
        )

        contentResolver.registerContentObserver(
            Students.Student().CONTENT_URI_STUDENT,
            true,
            studentObserver
        )
    }

    private inner class StudentObserver(handler: Handler) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            println(selfChange)
            inflateData(searchCursor())
        }
    }

    private fun initListener() {
        btnAdd.setOnClickListener {
            var contentValues = ContentValues()
            contentValues.put(Students.Student().NAME, etName.text.toString())
            contentValues.put(Students.Student().AGE, etAge.text.toString().toInt())
            contentResolver.insert(Students.Student().CONTENT_URI_STUDENTS, contentValues)
            Toast.makeText(this@ContentResolverActivity, "添加用户信息成功", Toast.LENGTH_SHORT)
        }

        btnUpdate.setOnClickListener {
            var contentValues = ContentValues()
            contentValues.put(Students.Student().AGE, 66)
            contentResolver.update(
                ContentUris.withAppendedId(Students.Student().CONTENT_URI_STUDENT, 2),
                contentValues,
                null,
                null
            )
        }

        btnSearch.setOnClickListener {
            inflateData(searchCursor())
        }
    }

    private fun searchCursor(): Cursor? {
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(
                Students.Student().CONTENT_URI_STUDENTS,
                arrayOf(Students.Student().NAME, Students.Student().AGE),
                "${Students.Student().NAME} like ? or ${Students.Student().AGE} like ?",
                arrayOf("%${etSearch.text.toString()}%", "%${etSearch.text.toString()}%"),
                null
            )
        } catch (e: Exception) {
        }
        return cursor
    }

    private fun inflateData(cursor: Cursor?) {
        if (cursor != null) {
            dataList.clear()
            while (cursor.moveToNext()) {
                val name = cursor.getString(0)
                val age = cursor.getInt(1)
                dataList.add(InfoModel(name, age))
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(studentObserver)
    }
}
