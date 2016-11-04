package com.random.xianarmedpolice.pager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.random.xianarmedpolice.R;
import com.random.xianarmedpolice.domain.NewsDetailBean;
import com.random.xianarmedpolice.utils.Constant;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/18.
 */
public class PoliticalDepartmentPager extends Basepager {


    public PoliticalDepartmentPager(Activity activity, String url) {
        super(activity, url);

    }

}
