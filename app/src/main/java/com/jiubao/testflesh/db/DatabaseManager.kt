package com.jiubao.testflesh.db

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import com.jiubao.testflesh.db.table.BaseTable
import com.jiubao.testflesh.db.table.impl.BaseTableImpl

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

class DatabaseManager(context: Context? = null){

    companion object {
        private val mTableList = ArrayList<BaseTableImpl>()

        @JvmStatic
        fun getInstance(context: Context? = null): DatabaseManager? {
            return DatabaseManager(context)
        }

        @JvmStatic
        fun <T : BaseTable> create(dataBaseHelper: DataBaseHelper,obj:T){}

        @JvmStatic
        fun <T: BaseTable> getById(dataBaseHelper: DataBaseHelper,obj: T){}

        @JvmStatic
        fun <T: BaseTable> update(dataBaseHelper: DataBaseHelper,obj: T){}

        @JvmStatic
        fun <T: BaseTable> delete(dataBaseHelper: DataBaseHelper,obj: T){}

    }

    private var mContext: Context? = null
    private var mDatabaseHelper: DataBaseHelper? = null
    init {
        if(context != null){
            mContext = context
        }
    }

    @JvmOverloads
    fun getHelper(context: Context,name:String,version:Int = 1,
                  factory: SQLiteDatabase.CursorFactory? = null,
                  errorHandler: DatabaseErrorHandler? = null): DataBaseHelper? {
        mDatabaseHelper = DataBaseHelper(context,name,factory,if(version >= 1) version else 0,errorHandler)
        mDatabaseHelper?.setTables(mTableList)
        return mDatabaseHelper;
    }

    fun getDatabase(): SQLiteDatabase? {
        return getDatabase(2)
    }

    fun getDatabase(version: Int):SQLiteDatabase? {
        if(mContext != null){
            return getHelper(mContext!!,"heaven",version)?.writableDatabase
        }
        return null
    }

    fun getTable(): List<BaseTable>{
        return mTableList;
    }

    fun <T: BaseTableImpl> registerTable(obj: T) {
        if(getTable().indexOf(obj) < 0){
            (getTable() as MutableList<BaseTable>).add(obj)
        }
    }
}






















