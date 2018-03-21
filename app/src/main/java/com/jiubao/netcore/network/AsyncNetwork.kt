package com.jiubao.netcore.network

import java.lang.Exception
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/21.
 * 版本:
 */

class AsyncNetwork:BaseNetWork(){
    companion object {
        private const val TAG = "AsyncNetwork"

        private var threadsCount:AtomicInteger = AtomicInteger(0)

        @JvmStatic
        private val fixedThreadPool = ThreadPoolExecutor(1,
                Runtime.getRuntime().availableProcessors() + 1,
                30L,TimeUnit.SECONDS,LinkedBlockingQueue())
    }

    private var future:Future<*>? = null

    override fun request(urlStr: String, mutableMap: MutableMap<String, String>?): BaseNetWork {
        future = fixedThreadPool.submit{
            try {
                super.request(urlStr, mutableMap)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        return this
    }

    override fun cancel() {
        super.cancel()
        future?.cancel(true)
    }
}