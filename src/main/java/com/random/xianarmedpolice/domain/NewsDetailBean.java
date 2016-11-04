package com.random.xianarmedpolice.domain;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/18.
 */
public class NewsDetailBean {

    public NewsTabMenuData data;

    @Override
    public String toString() {
        return "NewsTabMenu{" +
                "data=" + data +
                '}';
    }

    public static class NewsTabMenuData {

        public String more;
        public ArrayList<NewsTabMenuDataNews> news;
        public ArrayList<NewsTabMenuDataTopnews> topnews;


        @Override
        public String toString() {
            return "NewsTabMenuData{" +
                    "more='" + more + '\'' +
                    ", news=" + news +
                    ", topnews=" + topnews +
                    '}';
        }

        /**
         * 新闻列表对象
         */
        public static class NewsTabMenuDataNews {

            public int id;
            public String listimage;
            public String pubdate;
            public String title;
            public String type;
            public String url;

            @Override
            public String toString() {
                return "NewsTabMenuDataNews{" +
                        "listimage='" + listimage + '\'' +
                        ", title='" + title + '\'' +
                        '}';
            }
        }


        /**
         * 头条新闻对象,显示图片
         */
        public static class NewsTabMenuDataTopnews {


            public int id;
            public String topimage;
            public String pubdate;
            public String title;
            public String type;
            public String url;

            @Override
            public String toString() {
                return "NewsTabMenuDataTopnews{" +
                        "topimage='" + topimage + '\'' +
                        ", title='" + title + '\'' +
                        '}';
            }
        }
    }
}
