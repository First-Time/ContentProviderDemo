package com.lyf.contentresolver

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var mAdapter: BasicAdapter
    private var dataList: MutableList<InfoModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        mAdapter = BasicAdapter(dataList)
        recyclerView.adapter = mAdapter

        initListener()
    }

    private fun initListener() {
        btnAdd.setOnClickListener {
            var contentValues = ContentValues()
            contentValues.put(Students.Student().NAME, etName.text.toString())
            contentValues.put(Students.Student().AGE, etAge.text.toString().toInt())
            contentResolver.insert(Students.Student().CONTENT_URI_STUDENTS, contentValues)
            Toast.makeText(this@MainActivity, "添加用户信息成功", Toast.LENGTH_SHORT)
        }

        btnSearch.setOnClickListener {
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
            inflateData(cursor)
        }
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
}
