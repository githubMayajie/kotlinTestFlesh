package com.jiubao.netcore.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

public class PageDetailModel {
    int id;
    String baseUrl;
    int maxLen;
    String imgUrl;
    List<String> backupImgUrl;

    public PageDetailModel(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
        String[] temp = null;
        if(backupImgUrl != null){
            temp = backupImgUrl.toArray(new String[]{});
        }
        backupImgUrl = new ArrayList<>();
        for (int i = 0; i < maxLen; i++) {
            backupImgUrl.add("");
        }
        if(temp != null){
            for (int i = 0; i < temp.length; i++) {
                backupImgUrl.set(i,temp[i]);
            }
        }
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getBackupImgUrl() {
        return backupImgUrl;
    }

    public void setBackupImgUrl(List<String> backupImgUrl) {
        this.backupImgUrl = backupImgUrl;
    }

}
