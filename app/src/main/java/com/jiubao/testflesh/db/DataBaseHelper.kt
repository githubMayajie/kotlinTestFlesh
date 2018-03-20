package com.jiubao.testflesh.db

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.jiubao.testflesh.db.table.BaseTable

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

class DataBaseHelper : SQLiteOpenHelper{
    companion object {
        private const val TAG = "DataBaseHelper"
    }

    constructor(context: Context,name:String,
                factory:SQLiteDatabase.CursorFactory?,version:Int):
            super(context,name,factory,version)

    constructor(context: Context,name: String,factory: SQLiteDatabase.CursorFactory?,
                version: Int,errorHandler: DatabaseErrorHandler?):
            super(context,name,factory,version,errorHandler)

    private var mTable:List<BaseTable>? = null

    fun setTables(tables:List<BaseTable>){
        mTable = tables;
    }


    override fun onCreate(db: SQLiteDatabase?) {
        if(mTable != null){
            for (table in mTable!!){
                table.createTable(db!!);
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if(mTable != null){
            for (table in mTable!!){
                table.updateTable(db!!,p1,p2);
            }
        }
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        if(db?.isReadOnly() == false){// Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;")
        }
    }

}