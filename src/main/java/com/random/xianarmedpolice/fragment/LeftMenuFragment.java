package com.random.xianarmedpolice.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.random.xianarmedpolice.MainActivity;
import com.random.xianarmedpolice.R;
import com.random.xianarmedpolice.domain.NewsMenu;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/18.
 */
public class LeftMenuFragment extends Fragment {

    private Activity mActivity;

    @ViewInject(R.id.lv_menu)
    ListView lvMenu;

    private ArrayList<NewsMenu.NewsMenuData> menuData; //侧边栏数据Bean
    private int mLastPos; // 记录点击的位置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();
    }

    /**
     * 加载侧边栏ListVIew
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return initView();
    }

    /**
     * 设置侧边栏界面
     *
     * @return
     */
    private View initView() {

        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this, view);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    /**
     * 加载侧边栏
     */
    private void initData() {
    }

    /**
     * 设置侧边栏数据
     *
     * @param data
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {

        // 由主页面传来侧边栏数据
        this.menuData = data;


        // 设置样式
        final MenuAdapter menuAdapter = new MenuAdapter();
        lvMenu.setAdapter(menuAdapter);

        // ListView点击事件
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mLastPos = position;

                menuAdapter.notifyDataSetChanged();

                toggle();

                // 设置主页面内容LeftFragment --> ContentFragment
                setContentData(position);
            }
        });

    }

    /**
     * 在侧边栏中，设置主页面显示的内容
     * @param position
     */
    private void setContentData(int position) {

        ((MainActivity)mActivity).getContentFragment().setContentData(position);
    }

    /**
     * 打开收起侧边栏
     */
    private void toggle() {

        ((MainActivity) mActivity).getSlidingMenu().toggle();
    }

    /**
     * 设置ListView样式
     */
    private class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return menuData.get(position);
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
                view = View.inflate(mActivity, R.layout.fragment_left_menu_item, null);
            }

            // 设置侧边栏标题
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            tvMenu.setText(getItem(position).title);

            if (position == mLastPos) {
                // 被选中状态
                tvMenu.setEnabled(true);// 文字变为红色
            } else {
                // 未选中状态
                tvMenu.setEnabled(false);// 文字变为白色
            }

            return view;
        }
    }
}
