package com.jiubao.parcel.base;

import android.os.Parcel;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/21.
 * 版本:
 */

public abstract class ParcelableFileCacheHelper extends FileCacheHelper {

    private ByteArrayOutputStream byteArrayOutputStream;
    public ParcelableFileCacheHelper(String path) {
        super(path);
    }

    public int getVersion(){return 1;}
    public abstract <T> Parcel wrireParcel(Parcel parcel,T object);
    public abstract <T> T readParcel(Parcel parcel);
    private void gc(){
        System.gc();
        System.runFinalization();
        System.gc();
    }

    @Override
    protected <T> T readObjectFromStream(FileInputStream is) throws IOException, ClassNotFoundException {
        byteArrayOutputStream = new ByteArrayOutputStream();
        int len;
        byte[] buf = new byte[1024 * 1024];
        while ((len = is.read(buf)) > 0){
            byteArrayOutputStream.write(buf,0,len);
        }
        Parcel parcel = Parcel.obtain();
        buf = byteArrayOutputStream.toByteArray();
        parcel.unmarshall(buf,0,buf.length);
        parcel.setDataPosition(0);
        int version = parcel.readInt();
        if(version != getVersion()){
            if(getVersion() < 0){
                throw new IllegalStateException("gerVersion() must be > 0");
            }
            return null;
        }
        T ret = readParcel(parcel);
        parcel.recycle();
        gc();
        byteArrayOutputStream.close();
        return ret;
    }


    @Override
    protected <T> void writeObjectFromStream(FileOutputStream os, T object) throws IOException, ClassNotFoundException {
//        super.writeObjectFromStream(os, object);
        Parcel parcel = Parcel.obtain();
        parcel.writeInt(getVersion());
        parcel = wrireParcel(parcel,object);
        if(parcel != null){
            os.write(parcel.marshall());
            parcel.recycle();
        }
        gc();
    }

    @Override
    protected <T> T readObject(String key) {
        T ret = super.readObject(key);
        if(byteArrayOutputStream != null){
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byteArrayOutputStream = null;
        }
        return ret;
    }

    @Override
    protected <T> boolean writeObject(String key, T object) {
        return super.writeObject(key, object);
    }
}
























