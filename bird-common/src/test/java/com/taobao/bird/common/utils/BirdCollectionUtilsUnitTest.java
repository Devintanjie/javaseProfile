package com.taobao.bird.common.utils;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;

/**
 * @desc
 * @author junyu 2015年2月17日上午11:53:53
 * @version
 **/
public class BirdCollectionUtilsUnitTest {

    @Test
    public void changeJsonArrayToList() {
        JSONArray a = new JSONArray();
        a.add("a");
        a.add("c");
        a.add("b");

        List<String> s = BirdCollectionUtils.changeJsonArrayToList(a);
        Assert.assertEquals(a.size(), s.size());
        for (int i = 0; i < s.size(); i++) {
            Assert.assertEquals(a.get(i), s.get(i));
        }
    }
}
