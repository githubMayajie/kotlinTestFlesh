package com.jiubao.netcore.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

/**
 * 功能描述:
 * 作者： Administrator 时间： 2018/3/20.
 * 版本:
 */

public interface ISoup {
    void parse(Document root, Element head,Element body,Map<String,Object> values);
    Map<String,Object> doParse(Object... arg);
}
