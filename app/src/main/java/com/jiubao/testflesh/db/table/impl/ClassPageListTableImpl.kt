package com.jiubao.testflesh.db.table.impl

import android.database.sqlite.SQLiteDatabase

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

class ClassPageListTableImpl: BaseTableImpl(){
    override val sql: String
        get() = "CREATE TABLE tb_class_page_list (\n" +
                "    _id           INTEGER PRIMARY KEY ASC AUTOINCREMENT,\n" +
                "    href          STRING  UNIQUE,\n" +
                "    description   STRING,\n" +
                "    image_url     STRING,\n" +
                "    id_class_page INTEGER REFERENCES tb_class_page (_id) ON DELETE CASCADE\n" +
                "                                                         ON UPDATE CASCADE,\n" +
                "    [index]       INTEGER\n" +
                ");\n"

    companion object {
        const val TABLE_NAME = "tb_class_page_list"
    }

    override fun createTable(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(sql)
    }

    override fun deleteTable(sqLiteDatabase: SQLiteDatabase) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateTable(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        createTable(sqLiteDatabase)
    }

    fun addPageList(sqLiteDatabase: SQLiteDatabase,pageModel: PageM){

    }
}