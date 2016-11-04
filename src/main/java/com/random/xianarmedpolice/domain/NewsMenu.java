package com.random.xianarmedpolice.domain;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/18.
 * <p/>
 * 新闻侧边栏Bean
 */
public class NewsMenu {


    public ArrayList<NewsMenuData> data;

    @Override
    public String toString() {
        return "NewsMenu{" +
                "data=" + data +
                '}';
    }

    public static class NewsMenuData {

        public int id;
        public String title;
        public String url;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
