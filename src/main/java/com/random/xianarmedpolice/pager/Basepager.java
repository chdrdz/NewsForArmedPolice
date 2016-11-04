package com.random.xianarmedpolice.pager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.random.xianarmedpolice.NewsDetailActivity;
import com.random.xianarmedpolice.R;
import com.random.xianarmedpolice.domain.NewsDetailBean;
import com.random.xianarmedpolice.utils.Constant;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/18.
 */
public abstract class Basepager {


    public View mRootView;
    public Activity mActivity;

    public ViewPager vpNews;
    public ListView lvNews;
    public TextView tvDesc;
    public CirclePageIndicator indicator;

    public final String mUrl; //访问服务器获取新闻数据地址
    public NewsDetailBean mNewsDetail; // 所有新闻内容Bean
    public ArrayList<NewsDetailBean.NewsTabMenuData.NewsTabMenuDataTopnews> mTopNews; //头条新闻Bean
    public ArrayList<NewsDetailBean.NewsTabMenuData.NewsTabMenuDataNews> mNews; //新闻Bean

    public Handler mHandler;

    public Basepager(Activity activity, String url) {

        mActivity = activity;
        mRootView = initView();
        mUrl = Constant.SERVER_URL + url;
    }


    public View initView() {

        // 加载样式
        View view = View.inflate(mActivity, R.layout.fragment_news, null);
        View headerView = View.inflate(mActivity, R.layout.viewpager_topnews, null);

        // 控件初始化
        lvNews = (ListView) view.findViewById(R.id.lv_news);
        vpNews = (ViewPager) headerView.findViewById(R.id.vp_news);
        tvDesc = (TextView) headerView.findViewById(R.id.tv_desc);
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.vp_Indicator);

        // 1.ListView加载头部
        lvNews.addHeaderView(headerView);

        // 2.Viewpager设置点击事件，触摸时不再轮询
        vpNews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        mHandler.removeCallbacksAndMessages(null); //取消handler所有消息
                        break;

                    case MotionEvent.ACTION_UP:

                        mHandler.sendEmptyMessageDelayed(0, 3000);
                        break;

                    case MotionEvent.ACTION_CANCEL:

                        mHandler.sendEmptyMessageDelayed(0, 3000);
                        break;
                }


                return false;
            }
        });

        //3.设置ListView点击事件
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 改变字体颜色
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                // 进入WebView
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mNews.get(position - 1).url); //插图了头部，需要-1
                mActivity.startActivity(intent);
            }
        });


        //4.设置ViewPager滑动监听
        vpNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (mTopNews != null)
                    tvDesc.setText(mTopNews.get(position).title);
            }

            @Override
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    /**
     * 数据初始化
     */
    public void initData() {

        // 服务器获取数据
        getJSonFromServer();
    }

    /**
     * 从服务器获取数据 ,处理Json数据
     */
    private void getJSonFromServer() {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                ProcessJSon(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                e.printStackTrace();
            }
        });
    }

    private void ProcessJSon(String result) {

        // 1.JSon --> JavaBean
        Gson gson = new Gson();
        mNewsDetail = gson.fromJson(result, NewsDetailBean.class);
        System.out.println("新闻页面内容:" + mNewsDetail.toString());

        // 2.ViewPager加载数据
        mTopNews = mNewsDetail.data.topnews;
        vpNews.setAdapter(new TopNewsAdapter());

        //3.viewPager添加指示器
        indicator.setViewPager(vpNews);


        // 4.发送空消息，使得viewPager轮询
        if (mHandler == null) {

            mHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {

                    // 切换到下一页
                    int currentItem = vpNews.getCurrentItem();
                    vpNews.setCurrentItem(++currentItem % mTopNews.size());

                    // 轮询播放
                    sendEmptyMessageDelayed(0, 3000);
                }

            };
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }

        // 4.ListView加载数据
        mNews = mNewsDetail.data.news;
        lvNews.setAdapter(new NewsAdapter());
    }

    /**
     * ViewPager样式
     */
    class TopNewsAdapter extends PagerAdapter {

        private BitmapUtils bitmapUtils;

        TopNewsAdapter() {

            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {

            return mTopNews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //使用XUtils框架加载图片
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            bitmapUtils.display(imageView, mTopNews.get(position).topimage);
            container.addView(imageView);


            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }

    /**
     * 设置ListView样式
     */
    class NewsAdapter extends BaseAdapter {

        BitmapUtils bitmapUtils;

        NewsAdapter() {

            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public NewsDetailBean.NewsTabMenuData.NewsTabMenuDataNews getItem(int position) {
            return mNews.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;

            if (convertView != null) {

                view = convertView;
            } else {

                // 初始化控件
                view = View.inflate(mActivity, R.layout.listview_news, null);
            }

            // 控件初始化
            ImageView ivImage = (ImageView) view.findViewById(R.id.iv_list_image);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            TextView tvPubDate = (TextView) view.findViewById(R.id.tv_pub_date);

            // 设置标题,图片,发布时间
            tvTitle.setText(getItem(position).title);
            bitmapUtils.display(ivImage, getItem(position).listimage);
            tvPubDate.setText(getItem(position).pubdate);

            return view;
        }
    }
}
