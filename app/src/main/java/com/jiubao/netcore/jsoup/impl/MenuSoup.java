package com.jiubao.netcore.jsoup.impl;

import com.jiubao.netcore.jsoup.BaseSoup;
import com.jiubao.netcore.model.MenuModel;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

public class MenuSoup extends BaseSoup {

    private static final String TAG = "MenuSoup";

    public MenuSoup(String html) {
        super(html);
    }

    @Override
    public void parse(Document root, Element head, Element body, Map<String, Object> values) {
        Element set = body.getElementById("menu-ease-mobile");
        Elements childArr = set.getElementsByTag("a");
        List<MenuModel> models = new ArrayList<>();
        for (Element ele : childArr) {
            String title = ele.attr("title");
            String url = ele.attr("href");
            if (!title.equalsIgnoreCase("首页")) {
                MenuModel model = new MenuModel(title, url);
                models.add(model);
            }
        }
        //暂时去掉最后两个 每日更新 美女专题
        models.remove(models.size() - 1);
        models.remove(models.size() - 1);
        values.put(TAG, models);
    }
}
