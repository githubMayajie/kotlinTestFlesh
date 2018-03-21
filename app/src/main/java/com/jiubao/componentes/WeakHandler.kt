package com.jiubao.componentes

import android.os.Handler
import android.os.Message
import java.lang.ref.WeakReference


/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/21.
 * 版本:
 */

open class WeakHandler<T: WeakHandler.IHandleMessage>(host: T) : Handler(){

    private var weakRef: WeakReference<T>? = null

    init {
        weakRef = WeakReference(host)
    }

    override fun handleMessage(msg: Message?) {
        super.handleMessage(msg)
        if(weakRef?.get() == null )
            return
        weakRef?.get()?.handleMessage(msg!!)
    }

    interface IHandleMessage{
        fun handleMessage(msg:Message)
    }
}