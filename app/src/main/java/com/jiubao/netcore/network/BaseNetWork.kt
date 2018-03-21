package com.jiubao.netcore.network

import android.text.TextUtils
import org.jsoup.Connection
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

abstract class BaseNetWork{
    companion object {
        const val TIME_OUT = 5 * 1000
        const val CHARSET = "UTF-8"
        const val HEADER_CONTENT_ENCODING = "Content-Encoding"
        const val HEADER_CONTENT_LENGTH = "Content-Length"
        const val HTTP_PREFIX = "http://"
        const val CACHE_SIZE = 5 * 1024
        private const val TAG = "BaseNetwork"
    }

    object Method{
        const val OPTIONS = "OPTIONS"
        const val GET = "GET"
        const val HEAD = "HEAD"
        const val POST = "POST"
        const val PUT = "PUT"
        const val DELETE = "DELETE"
        const val TRACE = "TRACE"
    }


    private var callback: IRequestCallback? = null
    private var httpUrlConnection: HttpURLConnection? = null
    private var inputStream:InputStream? = null
    private var outputStream:OutputStream? = null
    private var header:HashMap<String,String>? = null
    private var mDoInput = true
    private var mDoOutput = true
    private var mUrl = ""
    var charset = Charset.forName("utf-8")

    protected open fun setupRequest(httpURLConnection: HttpURLConnection):BaseNetWork{
        httpURLConnection.apply {
            doInput = mDoInput
            doOutput = mDoOutput
            requestMethod = Method.GET
            connectTimeout = TIME_OUT
            readTimeout = TIME_OUT
            setRequestProperty("Content-type","*/*")
            setRequestProperty(HEADER_CONTENT_ENCODING, CHARSET)
            header?.apply {
                for (entry in this){
                    setRequestProperty(entry.key,entry.value)
                }
            }
        }
        return this;
    }

    fun setDoInputOutput(input:Boolean?,output:Boolean?):BaseNetWork{
        input?.let { mDoInput = input }
        output?.let { mDoOutput = output }
        return this
    }

    protected open fun setParams(httpURLConnection: HttpURLConnection,
                                 mutableMap: MutableMap<String, String>? = null):String{
        var ret = ""
        mutableMap?.let {
            httpURLConnection.requestMethod = Method.POST
            setDoInputOutput(null,true)
            var param = ""
            for(obj in mutableMap.entries){
                if(!TextUtils.isEmpty(param)){
                    param += "&"
                }
                param += "${obj.key}=${obj.value}"
            }
            httpURLConnection.setRequestProperty(HEADER_CONTENT_LENGTH,param.toByteArray().size.toString())
            ret = param
        }
        return ret
    }

    fun setRequestCallback(callback: IRequestCallback):BaseNetWork{
        this.callback = callback;
        return this;
    }

    @Throws(IOException::class)
    open fun connect(){
        try {
            httpUrlConnection?.connect()
        }catch (io:IOException){
            throw io
        }
    }

    protected fun pushContent(httpURLConnection: HttpURLConnection,param:String){
        if(httpURLConnection.requestMethod == Method.POST){
            if(!TextUtils.isEmpty(param)){
                outputStream = httpURLConnection.outputStream
                outputStream?.write(param.toByteArray())
                outputStream?.flush()
            }
        }
    }

    protected open fun getContent(httpURLConnection: HttpURLConnection):String{
        var ret = ""
        var os:ByteArrayOutputStream? = null
        try {
            os = ByteArrayOutputStream()
            var temp = ByteArray(CACHE_SIZE,{index -> 0})
            var `is` = httpURLConnection.inputStream
            inputStream = `is`
            var len:Int
            len = `is`.read(temp)
            while (len > 0){
                os.write(temp,0,len)
                len = `is`.read(temp)
            }
            ret = String(os.toByteArray(),charset)
            os.close()
        }catch (ex: Exception){
            if(inputStream != null){
                inputStream?.close()
            }
            if(os != null){
                os.close()
            }
            ex.printStackTrace()
            throw ex
        }

        return ret;
    }

    open fun setHeader(key:String,value:String):BaseNetWork{
        header?.put(key,value)
        return this
    }

    open fun setHeaders(values: HashMap<String,String>):BaseNetWork{
        if(header == null){
            header = HashMap<String,String>()
        }
        for(entry in values){
            setHeader(entry.key,entry.value)
        }
        return this
    }

    open fun cancel(){
        try {
        outputStream?.close()
        inputStream?.close()
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            httpUrlConnection?.disconnect()
        }
    }


    @JvmOverloads
    open fun request(urlStr:String,mutableMap: MutableMap<String,String>? = null):BaseNetWork{
        var ex: Exception? = null
        var ret = ""
        try {
            mUrl = urlStr;
            var url = URL(mUrl);
            httpUrlConnection = url.openConnection() as HttpURLConnection
            setupRequest(httpUrlConnection!!)
            var parseStr = setParams(httpUrlConnection!!,mutableMap)
            connect()
            pushContent(httpUrlConnection!!,parseStr)
            ret = getContent(httpUrlConnection!!)
        }catch (e:Exception){
            e.printStackTrace()
            ex = e
        }finally {
            if(ex != null && callback is IRequestCallbackV2){
                callback?.let {
                    (callback as IRequestCallbackV2).onError(httpUrlConnection,ex!!)
                }
            }else{
                callback?.onSuccess(httpUrlConnection,ret)
            }
        }
        return this
    }


}






















