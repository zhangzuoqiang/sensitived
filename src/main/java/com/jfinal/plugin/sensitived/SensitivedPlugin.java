package com.jfinal.plugin.sensitived;

import com.jfinal.plugin.IPlugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SensitivedPlugin implements IPlugin {

    private static final String WORD_FILE = "sensitived.txt";

    public SensitivedPlugin() {
    }

    @Override
    public boolean start() {
        // 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
        try {
            // 读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            // 将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br> 中 = { isEnd = 0 国 = {<br> isEnd = 1 人 = {isEnd = 0 民 =
     * {isEnd = 1} } 男 = { isEnd = 0 人 = { isEnd = 1 } } } } 五 = { isEnd = 0 星 = { isEnd = 0 红 = {
     * isEnd = 0 旗 = { isEnd = 1 } } } }
     *
     * @param keyWordSet 敏感词库
     */
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        SensitivewordFilter.getInstance().sensitiveWordMap = new HashMap(keyWordSet.size()); // 初始化敏感词容器，减少扩容操作
        String key = null;
        Map<Object, Object> nowMap = null;
        Map<Object, Object> newWorMap = null;
        // 迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next(); // 关键字
            nowMap = SensitivewordFilter.getInstance().sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i); // 转换成char型
                Object wordMap = nowMap.get(keyChar); // 获取
                if (wordMap != null) { // 如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else { // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<Object, Object>();
                    newWorMap.put("isEnd", "0"); // 不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1"); // 最后一个
                }
            }
        }
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     *
     * @return set
     */
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        InputStream in = SensitivedPlugin.class.getResourceAsStream("/" + WORD_FILE);
        if (in == null) {
            throw new Exception("敏感词库文件不存在，请确认是敏感词文件 " + WORD_FILE + " 是否进行配置！");
        }
        InputStreamReader read = new InputStreamReader(in, Charset.forName("UTF8"));
        try {
            set = new HashSet<String>();
            BufferedReader bufferedReader = new BufferedReader(read);
            String txt = null;
            while ((txt = bufferedReader.readLine()) != null) { // 读取文件，将文件内容放入到set中
                set.add(txt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            read.close(); // 关闭文件流
        }
        return set;
    }

}
