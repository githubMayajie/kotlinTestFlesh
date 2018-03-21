package com.jiubao.netcore.model;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

public class MenuModel {
    int id;
    String title;
    String url;

    public MenuModel(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MenuModel))
            return false;
        MenuModel local = (MenuModel) obj;
        if(url.equals(local.url)){
            return true;
        }else {
            return false;
        }
    }
}
