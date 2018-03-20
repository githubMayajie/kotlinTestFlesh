package com.jiubao.testflesh.db.table.impl

import com.jiubao.testflesh.db.table.BaseTable

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

abstract class BaseTableImpl: BaseTable{
    override fun equals(other: Any?): Boolean {
        if(other !is BaseTableImpl){
            return false
        }
        return this.sql == other.sql
    }

    override fun hashCode(): Int {
        return this.sql.hashCode();
    }
}