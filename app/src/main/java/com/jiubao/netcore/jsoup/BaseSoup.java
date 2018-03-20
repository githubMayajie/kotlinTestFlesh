package com.jiubao.netcore.jsoup;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

public abstract class BaseSoup implements ISoup {
    private String html;
    private Map<String,Object> value;
    protected Element header;
    protected Element body;
    private Object[] arguments;

    public BaseSoup(String html) {
        this.html = html;
    }

    @Override
    public Map<String, Object> doParse(Object... arg) {
        arguments = arg;
        if(value == null)
            value = new HashMap<>();
        Document doc = Jsoup.parse(html);
        header = doc.head();
        body = doc.body();
        parse(doc,header,body,value);
        return value;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
