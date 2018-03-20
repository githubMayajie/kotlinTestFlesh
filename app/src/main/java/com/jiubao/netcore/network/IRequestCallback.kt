package com.jiubao.netcore.network

import java.lang.Exception
import java.net.HttpURLConnection

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

interface IRequestCallback{
    fun onSuccess(httpURLConnection : HttpURLConnection?,response:String)
}

interface IRequestCallbackV2: IRequestCallback{
    fun onError(httpURLConnection: HttpURLConnection?,exception:Exception)
}