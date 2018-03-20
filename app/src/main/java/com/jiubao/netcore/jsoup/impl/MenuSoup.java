package com.jiubao.netcore.jsoup.impl;

import com.jiubao.netcore.jsoup.BaseSoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        List<MenuModel>
    }
}
