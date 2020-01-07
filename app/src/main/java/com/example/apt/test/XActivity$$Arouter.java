package com.example.apt.test;

import com.example.apt.MainActivity;

/**
 * 北京中油瑞飞信息技术有限责任公司 研究院 瑞信项目All Rights Reserved
 *
 * @author : Chengjs
 * @项目:瑞信项目
 * @类:XActivity$$Arouter
 * @描述:
 * @版本信息：
 * @date : 2020-01-06 12:19
 */
public class XActivity$$Arouter {

    public static Class findTarget(String path) {
        if (path.equalsIgnoreCase("/app/MainActicity")) {
            return MainActivity.class;
        }
        return null;

    }
}
