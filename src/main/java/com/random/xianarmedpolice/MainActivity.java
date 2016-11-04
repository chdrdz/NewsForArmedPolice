package com.random.xianarmedpolice;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.random.xianarmedpolice.fragment.ContentFragment;
import com.random.xianarmedpolice.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

    private String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private String TAG_CONTENT = "TAG_CONTENT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化主页面
        setContentView(R.layout.activity_main);

        // 初始化侧边栏控件
        setBehindContentView(R.layout.fragment_left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
        slidingMenu.setBehindOffset(400);// 屏幕预留400像素宽度

        // 初始化两个Fragment，侧边栏和主页
        initFragment();
    }

    /**
     * 初始化侧边栏、主页Fragment
     */
    private void initFragment() {

        // 1.获取管理器
        FragmentManager fm = getSupportFragmentManager();

        // 2.开始事务
        FragmentTransaction transaction = fm.beginTransaction();

        // 3.替换侧边栏
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        // 3.替换主页面布局
        transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);

        // 4.提交事务
        transaction.commit();
    }

    /**
     * 获取左侧边栏
     *
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {

        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(TAG_LEFT_MENU);
    }

    /**
     * 获取主页ContentFragment
     */
    public ContentFragment getContentFragment() {

        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(TAG_CONTENT);
    }
}
