package com.jfinal.plugin.sensitived.test;

import com.jfinal.plugin.sensitived.SensitivedPlugin;
import com.jfinal.plugin.sensitived.SensitivedFilter;

import org.junit.Test;

import java.util.Set;

/**
 * 敏感词过滤测试
 */
public class SensitivedPluginTest {

    @Test
    public void test() {
        SensitivedPlugin plugin = new SensitivedPlugin();
        plugin.start();

        System.out.println("敏感词树的数量：" + SensitivedFilter.getInstance().sensitiveWordMap.size());
        String string = "这个是测试的字符串，中国人，是好人。五星的中国女人。中国人民啊。张首席，首席张，张瑞敏， 黑心家电";
        System.out.println("待检测语句字数：" + string.length());
        long beginTime = System.currentTimeMillis();
        Set<String> set = SensitivedFilter.getInstance().getSensitiveWord(string, 2);
        long endTime = System.currentTimeMillis();
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        System.out.println("总共消耗时间为：" + (endTime - beginTime));
    }
}
