package com.jiubao.testflesh;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(!isApplicationProcess("com.jiubao.testflesh")){
            initGlide();

            initDb();
            initSDK();
            init();
        }else {
            if(!getAppNameByPID(this,android.os.Process.myPid()).endsWith("msg")){
                initGlide();
            }
        }
    }

    private void initDb(){

    }

    private void initGlide(){
        SimpleGlideModule module = new SimpleGlideModule();
        GlideBuilder builder = new GlideBuilder();
        module.applyOptions(this,builder);
        Glide glide = builder.build(this);
        Glide.init(glide);
    }


    public String getAppNameByPID(Context context,int pid){
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()){
            if(processInfo.pid == pid){
                return processInfo.processName;
            }
        }
        return "";
    }
    public boolean isApplicationProcess(String packageName){
        int pid = android.os.Process.myPid();
        String process = getAppNameByPID(this,pid);
        return packageName.equals(process);
    }

    private static class SimpleGlideModule extends AppGlideModule{
        @Override
        public void applyOptions(Context context, GlideBuilder builder) {
            long diskCacheSize = Constants.DEFAULT_GLIDE_CACHE_SIZE;
            builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context,(int)diskCacheSize));
            builder.setMemoryCache(new LruResourceCache(24 * 1024 * 1024));
        }
    }
}
