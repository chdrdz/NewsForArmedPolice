package com.random.xianarmedpolice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_back)
    ImageButton btnBack;
    @ViewInject(R.id.btn_text_size)
    ImageButton btnTextSize;
    @ViewInject(R.id.btn_share)
    ImageButton btnShare;
    @ViewInject(R.id.wv_news_detail)
    WebView wvNewsDetail;
    @ViewInject(R.id.pb_loading)
    ProgressBar pbLoading;

    private int mTempWhich = 2; //选择的字体大小(默认正常字体)
    private int mCurrentWhich; // 最终选择的字体大小
    private WebSettings settings; //WebView设置参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ViewUtils.inject(this);

        //1. 获取Intent传递数据
        String url = getIntent().getStringExtra("url");

        //2.加载页面
        wvNewsDetail.loadUrl(url);

        // 添加点击事件
        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnTextSize.setOnClickListener(this);

        //3.设置webView参数
        settings = wvNewsDetail.getSettings();
        settings.setBuiltInZoomControls(true); // 显示缩放按钮
        settings.setUseWideViewPort(true); // 双击缩放
        settings.setJavaScriptEnabled(true); //设置支持JS

        // 4.设置不调用系统浏览器打开网页
        wvNewsDetail.setWebViewClient(new WebViewClient() {

            /**
             * 网页开始加载
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                pbLoading.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                pbLoading.setVisibility(View.INVISIBLE);
                super.onPageFinished(view, url);
            }

            /**
             * 跳转页面的方法
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url); //跳转页面强制在当前WebView加载
                return true;
            }
        });

        //5.浏览器设置，加载进度
        wvNewsDetail.setWebChromeClient(new WebChromeClient() {

            /**
             * 进度发生变化
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 接收到标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_back:

                finish();
                break;

            case R.id.btn_text_size:

                setTextSizeDialog();
                break;

            case R.id.btn_share:

                share();
                break;
        }
    }

    /**
     * 通过ShareSDK进行分享
     */
    private void share() {

        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    /**
     * 改变字体大小
     */
    private void setTextSizeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");

        // 设置单选对话框
        builder.setSingleChoiceItems(new String[]{"超大字体", "大号字体", "正常字体", "小号字体", "超小号字体"}, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mTempWhich = which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mCurrentWhich = mTempWhich; // 最终选择的字体大小

                switch (mTempWhich) {

                    case 0: //超大字体

                        settings.setTextZoom(132);
                        break;

                    case 1: //大号字体

                        settings.setTextZoom(120);
                        break;

                    case 2: //正常字体

                        settings.setTextZoom(108);
                        break;

                    case 3: //小号字体

                        settings.setTextZoom(96);
                        break;

                    case 4: //超小号字体

                        settings.setTextZoom(84);
                        break;
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
