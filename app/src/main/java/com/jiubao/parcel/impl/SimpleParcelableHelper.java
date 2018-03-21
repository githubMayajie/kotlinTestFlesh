package com.jiubao.parcel.impl;

import android.os.Parcel;
import android.os.Parcelable;

import com.jiubao.parcel.base.ParcelableFileCacheHelper;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/21.
 * 版本:
 */

public class SimpleParcelableHelper extends ParcelableFileCacheHelper {
    public SimpleParcelableHelper(String path) {
        super(path);
    }

    @Override
    public <T> Parcel wrireParcel(Parcel parcel, T object) {
        if(object instanceof Parcelable){
            parcel.writeParcelable((Parcelable) object,0);
        }
        return parcel;
    }

    @Override
    public <T> T readParcel(Parcel parcel) {
        return parcel.readParcelable(getClass().getClassLoader());
    }
}
