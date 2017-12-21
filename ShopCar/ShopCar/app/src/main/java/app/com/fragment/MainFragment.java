package app.com.fragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.JsonToken;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.alibaba.fastjson.*;
import app.com.ConstantUtil;
import app.com.base.BaseFragment;
import app.com.httprequest.HttpRequest;
import app.com.httprequest.RequestThread;
import app.com.shopcar.MainActivity;
import app.com.shopcar.R;
import butterknife.Bind;

/**
 * A placeholder fragment containing a simple view.
 */
/**
 *第一个页面
 * Created by Administrator on 2017/12/1.
 */

public class MainFragment extends BaseFragment {

    @Bind(R.id.wv_fmain_show)
    WebView mWebView;

    private JSONObject mJsonData = new JSONObject();
    private JSONArray mJSONArray = new JSONArray();
    private String mdata ="";
    private Object postData[][]=new String[20][23];
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mdata = (String) msg.obj;
        }
    };
    private RequestThread mRequestThread=new RequestThread(mHandler);
    private Thread mThread= new Thread(mRequestThread);

    @Override
    protected int getContextLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
        super.initData();

        mThread.start();
        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mWebView.clearCache(true);

        WebSettings pWebSettings = mWebView.getSettings();
        // 设置webview支持js
        pWebSettings.setJavaScriptEnabled(true);
        // 设置本地调用对象及其接口
        mWebView.addJavascriptInterface(new JsInteraction(), "control");

        // 设置允许JS弹窗
        pWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置资源文件
        mWebView.loadUrl("file:///android_asset/ShopList.html",null);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                mWebView.post(new  Runnable() {

                    @Override
                    public void run() {

                        // 取网页数据
                        // 调用javascript的getData()方法


                        try {

                            mJsonData = JSON.parseObject(mdata);
                            //System.out.print("----------->"+mJsonData);

//                            Log.e("----------->", String.valueOf(mJsonData));
                            mJSONArray =  mJsonData.getJSONArray("books");
                            mWebView.loadUrl("javascript:getData()");
//                            Log.e("----------->>>", " {\"bookdetail\": ["+mJSONArray.getJSONObject(0).toString()+"]}");
                            for (int i = 0;i<postData.length;i++){
                                postData[i][0]=mJSONArray.getJSONObject(i).getString("summary");
                                postData[i][1]=mJSONArray.getJSONObject(i).getString("price");
                                postData[i][2]=mJSONArray.getJSONObject(i).getString("publisher");
                                postData[i][3]=mJSONArray.getJSONObject(i).getString("images");
                                postData[i][4]=mJSONArray.getJSONObject(i).getString("author");
                                postData[i][5]=mJSONArray.getJSONObject(i).getString("id");
                                postData[i][6]=mJSONArray.getJSONObject(i).getString("image");

                            }
                            //Log.e("====================>", String.valueOf(postData[0][1]));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });


        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        // 设置响应js 的Alert()函数
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setTitle("提示：");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();

                return true;
            }

        });

    }


    public class JsInteraction {
        private JSONObject mJsonData = new JSONObject();
        private JSONArray mJSONArray = new JSONArray();
        @RequiresApi(api = 26)
        @JavascriptInterface
        public void toData(String  message) {
            // 提供给js调用的方法
//            Log.e("---------<>",message);
//            WebView pWebView = (WebView) getActivity().getFragmentManager().getFragments().get(1).getView().findViewById(R.id.wv_ffollow_show);//获取B页面的控件
//            pWebView.loadUrl("javascript:gets()");
            ConstantUtil.DATA = message;
        }
        @JavascriptInterface
        public String getArray(){
//            mJsonData = JSON.parseObject(mdata);
//            mJSONArray =  mJsonData.getJSONArray("books");
//            mdata = mJSONArray.toJSONString();
//            Log.e("====================>", mdata);
            return mdata;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}