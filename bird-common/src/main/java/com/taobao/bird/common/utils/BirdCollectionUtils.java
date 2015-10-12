package com.taobao.bird.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

/**
 * @desc
 * @author junyu 2015年2月17日上午11:49:23
 * @version
 **/
public class BirdCollectionUtils {

    public static List<String> changeJsonArrayToList(JSONArray json) {
        List<String> result = new ArrayList<>();
        Iterator<Object> i = json.iterator();
        for (; i.hasNext();) {
            result.add(String.valueOf(i.next()));
        }

        return result;
    }
}
