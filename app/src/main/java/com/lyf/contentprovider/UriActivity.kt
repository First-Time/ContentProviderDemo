package com.lyf.contentprovider

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class UriActivity : AppCompatActivity() {
    private val TAG = "ContentProvider"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uri)

        val uriStr = "http://www.baidu.com:8080/wenku/jiatiao.html?id=123456&name=jack"
        val uri = Uri.parse(uriStr)
        println("$TAG scheme: ${uri.scheme}")
        println("$TAG host: ${uri.host}")
        println("$TAG port: ${uri.port}")
        println("$TAG path: ${uri.path}")
        println("$TAG encodedPath: ${uri.encodedPath}")
        println("$TAG query: ${uri.query}")
        println("$TAG encodedQuery: ${uri.encodedQuery}")

        val originalUri = Uri.parse("hehe://a/hehe?params=%7B%22url")
        val newUri = Uri.Builder().scheme("haha")
            .authority(originalUri.authority)
            .path(originalUri.path)
            .encodedQuery(originalUri.encodedQuery).build()
        Log.d(TAG, newUri.toString())
    }
}
