package com.jiubao.netcore.jsoup;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

public class SoupFactory {
    public static Map<String,Object> parseHtml(Class<? extends BaseSoup> clazz,String html,Object... arg){
        BaseSoup soup = null;
        try {
            soup = clazz.getConstructor(String.class).newInstance(html);
            return soup.doParse(arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String,Object> parseHtml(Class<? extends BaseSoup> clazz,String html){
        return parseHtml(clazz,html,new Object());
    }
}
