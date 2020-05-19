package com.lyf.contentresolver

import android.net.Uri
import android.provider.BaseColumns

class Students {
    companion object {
        const val AUTHORITY = "com.lyf.studentProvider"
        const val TABLE_NAME = "student_dict"
    }

    class Student : BaseColumns {
        val _ID = "_id"
        val NAME = "student_name"
        val AGE = "student_age"

        val CONTENT_URI_STUDENTS = Uri.parse("content://$AUTHORITY/students")
        val CONTENT_URI_STUDENT = Uri.parse("content://$AUTHORITY/student")
    }
}