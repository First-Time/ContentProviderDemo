package com.lyf.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.lyf.contentprovider.Students.Companion.AUTHORITY
import com.lyf.contentprovider.Students.Companion.TABLE_NAME

class StudentContentProvider : ContentProvider() {

    private val STUDENTS = 1
    private val STUDENT = 2

    private var uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private lateinit var studentOpenHelper: StudentOpenHelper

    init {
        uriMatcher.addURI(AUTHORITY, "students", STUDENTS)
        uriMatcher.addURI(AUTHORITY, "student/#", STUDENT)
    }

    override fun onCreate(): Boolean {
        studentOpenHelper = StudentOpenHelper(context, "studentDict.db3", 1)
        return true
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            STUDENTS -> "vnd.android.cursor.dir/com.lyf.student"
            STUDENT -> "vnd.android.cursor.item/com.lyf.student"
            else -> throw IllegalArgumentException("未知Uri：$uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (uriMatcher.match(uri)) {
            STUDENTS -> {
                val rowId = studentOpenHelper.readableDatabase.insert(TABLE_NAME, null, values)
                var studentUri: Uri? = null
                if (rowId > 0) {
                    studentUri = ContentUris.withAppendedId(uri, rowId)
                }
                studentUri
            }
            else -> throw IllegalArgumentException("未知Uri：$uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        var num: Int
        when (uriMatcher.match(uri)) {
            STUDENTS -> num =
                studentOpenHelper.writableDatabase.delete(TABLE_NAME, selection, selectionArgs)
            STUDENT -> {
                val id = ContentUris.parseId(uri)
                var whereClause = "${Students.Student()._ID} = $id"
                if (selection != null && selection != "") {
                    whereClause = "$whereClause and $selection"
                }
                num = studentOpenHelper.writableDatabase.delete(
                    TABLE_NAME,
                    whereClause,
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("未知Uri：$uri")
        }
        return num
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var num = 0
        when (uriMatcher.match(uri)) {
            STUDENTS -> num = studentOpenHelper.writableDatabase.update(
                TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
            STUDENT -> {
                val id = ContentUris.parseId(uri)
                var whereClause = "${Students.Student()._ID} = $id"
                if (selection != null && selection != "") {
                    whereClause = "$whereClause and $selection"
                }
                num = studentOpenHelper.writableDatabase.update(
                    TABLE_NAME,
                    values,
                    whereClause,
                    selectionArgs
                )
            }
        }
        return num
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            STUDENTS -> studentOpenHelper.readableDatabase.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            STUDENT -> {
                val id = ContentUris.parseId(uri)
                var whereClause = "${Students.Student()._ID} = $id"
                if (selection != null && selection != "") {
                    whereClause = "$whereClause and $selection"
                }
                studentOpenHelper.readableDatabase.query(
                    TABLE_NAME,
                    projection,
                    whereClause,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
            else -> throw IllegalArgumentException("未知Uri：$uri")
        }
    }
}
