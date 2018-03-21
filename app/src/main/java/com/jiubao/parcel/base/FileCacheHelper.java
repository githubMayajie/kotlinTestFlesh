package com.jiubao.parcel.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/21.
 * 版本:
 */

public class FileCacheHelper {
    private ReentrantReadWriteLock reentrantLock;
    private String path;
    private Lock readLock;
    private Lock wrireLock;

    public FileCacheHelper(String path) {
        this.path = path;
        reentrantLock = new ReentrantReadWriteLock();
        readLock = reentrantLock.readLock();
        wrireLock = reentrantLock.writeLock();
    }

    public <T> T get(String key){
        T ret = null;
        try {
            readLock.lockInterruptibly();
            ret = readObject(key);
        } catch (Exception e) {
            e.printStackTrace();
            ret = null;
        }finally {
            readLock.unlock();
        }
        return ret;
    }

    public boolean put(String key,Object object){
        boolean ret = false;
        try {
            wrireLock.lockInterruptibly();
            ret = readObject(key);
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }finally {
            wrireLock.unlock();
        }
        return ret;
    }

    //file write and read
    protected <T> T readObjectFromStream(FileInputStream is) throws
            IOException,ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(is);
        return (T)ois.readObject();
    }

    protected <T> void writeObjectFromStream(FileOutputStream os,T object) throws
            IOException,ClassNotFoundException{
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(object);
    }

    protected <T> T readObject(String key,String path){
        File file = new File(path,key);
        if(!file.exists())
            return null;
        T ret = null;
        FileLock fileLock = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fileLock = fis.getChannel().lock(0L,Long.MAX_VALUE,true);
            ret = readObjectFromStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileLock != null){
                try {
                    fileLock.release();
                }catch (IOException e){
                }
            }
            if(fis != null){
                try {
                    fis.close();
                }catch (Exception e){
                }
            }
        }
        return ret;
    }


    protected <T> boolean writeObject(String key,T object,String path,String tempName){
        File file = new File(path,tempName);
        if(file.exists())
            file.delete();
        FileOutputStream fos = null;
        boolean ret = false;
        FileLock fileLock = null;
        try {
            fos = new FileOutputStream(file);
            fileLock = fos.getChannel().lock();
            writeObjectFromStream(fos,object);
            ret = true;
            file.renameTo(new File(path,key));
        }catch (Exception e){
            e.printStackTrace();
            ret = false;
        }finally {
            if(fileLock != null){
                try {
                    fileLock.release();
                }catch (Exception e){
                }
            }
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    protected  <T> T readObject(String key){
        return readObject(key,path);
    }

    protected <T> boolean writeObject(String key,T object){
        return writeObject(key,object,path,key + "@@@@");
    }

    private boolean remove(String key){
        FileLock fileLock = null;
        FileInputStream fis = null;
        File file = null;
        boolean ret = false;

        try {
            readLock.lockInterruptibly();
            file = new File(path,key);
            fis = new FileInputStream(file);
            fileLock = fis.getChannel().lock(0L,Long.MAX_VALUE,true);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            readLock.unlock();
            if(fileLock != null){
                try {
                    fileLock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(file != null){
                ret = file.delete();
            }
        }
        return ret;
    }



}























