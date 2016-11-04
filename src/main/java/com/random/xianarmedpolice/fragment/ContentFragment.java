package com.random.xianarmedpolice.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.random.xianarmedpolice.MainActivity;
import com.random.xianarmedpolice.R;
import com.random.xianarmedpolice.domain.NewsMenu;
import com.random.xianarmedpolice.pager.Basepager;
import com.random.xianarmedpolice.pager.CampFivePager;
import com.random.xianarmedpolice.pager.CampFourPager;
import com.random.xianarmedpolice.pager.CampOnePager;
import com.random.xianarmedpolice.pager.CampSixPager;
import com.random.xianarmedpolice.pager.CampThreePager;
import com.random.xianarmedpolice.pager.CampTwoPager;
import com.random.xianarmedpolice.pager.CommandDepartmentPager;
import com.random.xianarmedpolice.pager.LogisticsDepartmentPager;
import com.random.xianarmedpolice.pager.PoliticalDepartmentPager;
import com.random.xianarmedpolice.utils.Constant;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ContentFragment extends Fragment {

    private Activity mActivity;

    @ViewInject(R.id.btn_menu)
    ImageButton btnSlideMenu;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.fl_content)
    FrameLayout flContent;

    private NewsMenu mNewsMenuBean; //侧边栏JavaBean
    private ArrayList<Basepager> mContentPager;
    private int mLastPos; //记录最后一次点击状态

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    /**
     * 加载主界面
     *
     * @return 返回主页面
     */
    private View initView() {

        // 1.初始化内容界面
        View view = View.inflate(mActivity, R.layout.fragment_content, null);

        //2.控件初始化
        ViewUtils.inject(this, view);

        // 3.设置打开关闭侧边栏按钮
        btnSlideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity) mActivity).getSlidingMenu().toggle();
            }
        });

        return view;
    }

    /**
     * 访问网络加载数据
     */
    private void initData() {


        // 访问服务器，获取新闻数据
        getJSonFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getJSonFromServer() {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constant.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                // 处理JSON字符串
                processJSon(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {

                e.printStackTrace();
                Toast.makeText(mActivity, "访问服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * JSON --> JavaBean
     */
    private void processJSon(String result) {

        // 1.解析JSon
        Gson gson = new Gson();
        mNewsMenuBean = gson.fromJson(result, NewsMenu.class);
        System.out.println("侧边栏数据:" + mNewsMenuBean.data.toString());

        //2.从服务器获取数据，设置侧边栏
        LeftMenuFragment leftMenuFragment = ((MainActivity) this.mActivity).getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsMenuBean.data);

        // 3.加载新闻主页面
        mContentPager = new ArrayList<>();
        mContentPager.add(new CommandDepartmentPager(mActivity, mNewsMenuBean.data.get(0).url));
        mContentPager.add(new PoliticalDepartmentPager(mActivity, mNewsMenuBean.data.get(1).url));
        mContentPager.add(new LogisticsDepartmentPager(mActivity, mNewsMenuBean.data.get(2).url));
        mContentPager.add(new CampOnePager(mActivity, mNewsMenuBean.data.get(3).url));
        mContentPager.add(new CampTwoPager(mActivity, mNewsMenuBean.data.get(4).url));
        mContentPager.add(new CampThreePager(mActivity, mNewsMenuBean.data.get(5).url));
        mContentPager.add(new CampFourPager(mActivity, mNewsMenuBean.data.get(6).url));
        mContentPager.add(new CampFivePager(mActivity, mNewsMenuBean.data.get(7).url));
        mContentPager.add(new CampSixPager(mActivity, mNewsMenuBean.data.get(8).url));

        setContentData(mLastPos == 0 ? 0 : mLastPos);
    }

    /**
     * 从侧边栏中设置主页面内容
     *
     * @param position
     */
    public void setContentData(int position) {

        mLastPos = position;

        flContent.removeAllViews();

        // 初始化子页面布局
        View mRootView = mContentPager.get(position).mRootView;

        // 改变标题
        tvTitle.setText(mNewsMenuBean.data.get(position).title);

        // 加载子页面数据
        mContentPager.get(position).initData();

        // 加载页面布局
        flContent.addView(mRootView);
    }
}
