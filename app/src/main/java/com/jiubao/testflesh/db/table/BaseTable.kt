package com.jiubao.testflesh.db.table

import android.database.sqlite.SQLiteDatabase

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */
interface BaseTable{
    val _id: String
        get() = "_id"
    val sql: String
    fun createTable(sqLiteDatabase: SQLiteDatabase)
    fun deleteTable(sqLiteDatabase: SQLiteDatabase)
    fun updateTable(sqLiteDatabase: SQLiteDatabase,oldVersion: Int,newVersion: Int)
}
