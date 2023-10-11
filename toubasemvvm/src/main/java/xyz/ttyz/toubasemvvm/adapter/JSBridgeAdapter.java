package xyz.ttyz.toubasemvvm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.databinding.BindingAdapter;

public class JSBridgeAdapter {
    private static final String TAG = "JSBridgeAdapter";

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @BindingAdapter(value = {"interfaceObj"})
    public static void webChromeClient(final WebView webView, Object interfaceObj) {
        webView.addJavascriptInterface(interfaceObj, "App");
    }

    @BindingAdapter(value = {"url"})
    public static void webChromeClient(final WebView webView, final String url) {
        Log.i(TAG, "webChromeClient: url -> " + url);
        webView.loadUrl(url);
    }

    @BindingAdapter("enableSetting")
    public static void enableSetting(WebView webView, boolean enableSetting) {
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);//返回不刷新LOAD_CACHE_ELSE_NETWORK
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        String appCachePath = /*webView.getContext().getCacheDir().getAbsolutePath()*/webView.getContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //以下两条设置可以使页面适应手机屏幕的分辨率，完整的显示在屏幕上
        settings.setUseWideViewPort(true);//设置是否使用WebView推荐使用的窗口
        settings.setLoadWithOverviewMode(true);// 设置WebView加载页面的模式
//        settings.setBlockNetworkImage(true);//不让图片加载
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        //region 支持pdf预览
//        settings.setAllowFileAccess(false);
        settings.setTextZoom(100);//如果是在自己开发的APP中，可以在客户端的WebView组件中设置字体默认的缩放比例，以避免手机系统的字体修改对页面字体及布局造成影响。
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        //endregion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @BindingAdapter("useBlockNetworkImage")
    public static void blockNetworkImage(WebView webView, boolean useBlockNetworkImage) {
        webView.getSettings().setBlockNetworkImage(useBlockNetworkImage);//不让图片加载
    }

    @BindingAdapter("webChromeClient")
    public static void wcc(WebView webView, WebChromeClient webChromeClient) {
        webView.setWebChromeClient(webChromeClient);
    }

    @BindingAdapter("htmlTxt")
    public static void html(WebView webView, String htmlTxt) {
        if (htmlTxt == null) return;
        ViewGroup.LayoutParams layoutParams = webView.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
//            marginLayoutParams.setMarginStart(DensityUtil.dip2px(15));
//            marginLayoutParams.setMarginEnd(DensityUtil.dip2px(15));
            webView.setLayoutParams(marginLayoutParams);
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setTextZoom(100);
        webView.loadDataWithBaseURL("about:blank", html1 + htmlTxt + html2, "text/html", "utf-8", null);//加载html数据
    }

    //    public static final String HTML_CSS = "<head><style> *{max-width: 100%;} img{max-width: 100%;} section{margin: 0 !important; width: 100% !important;}</style>
//    <meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no'/></head>";
    public static final String HTML_CSS = "<head>\n" +
            "    <meta name='viewport' content='width=device-width' />\n" +
            "    <style>\n" +
            "\n" +
            "     body{\n" +
            "       overflow-y: scroll;\n" +
            "       overflow-x: hidden;\n" +
            "       box-sizing: border-box;\n" +
            "       margin: 0;\n" +
            "       padding: 5px;\n" +
            "     }\n" +
            "\n" +
            "     *{\n" +
            "       font-size: 14px !important;\n" +
            "     }\n" +
            "\n" +
                "img{max-width: 100%;}"
            +
            "\n" +
            "     span{\n" +
            "       position: relative !important;\n" +
            "     }\n" +
            "\n" +
            "      img {\n" +
            "\n" +
            "        height: auto !important;\n" +
            "        object-fit: cover;\n" +
            "\n" +
            "      }\n" +
            "\n" +
            "\n" +
            "    </style>\n" +
            "  </head>";

    public static final String html1 = "<!DOCTYPE html>\n" +
            "<html>\n" + HTML_CSS +
            " <body>\n";

    public static final String html2 =
            "</body>\n" +
                    "</html>";

    public static String testHtml = "<div style=\"background-color: #000000;color:#ffffff\">\n" +
            "    <p style=\"text-align:justify;\"><font size=\"3\">家长都很关心孩子的学习，元宝爸爸也一样。</font></p ><p style=\"text-align:justify;\"><font size=\"3\">对于怎么激励孩子学习，元宝爸爸一直坚持“重赏之下必有勇夫”的观点，觉得要想孩子学习好，一定要重赏，最好能超出孩子的预期，这样孩子才会越来越爱学习，口头表扬什么的都是虚的。</font></p ><p style=\"text-align:justify;\"><font size=\"3\">但物质奖励的金额越来越大，元宝爸爸就有点受不住了。</font></p ><p style=\"text-align:justify;\"><font size=\"3\">他思考，到底要怎么正确鼓励孩子呢？</font></p ><img src=\"http://landun-v2.oss-cn-shanghai.aliyuncs.com/images/17898d2a5dd78b46ec000b649bc79537.JPG\" style=\"width:100%;object-fit:cover\">\n" +
            "   </div>";
}
