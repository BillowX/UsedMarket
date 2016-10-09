package com.maker.use.utils;

import com.maker.use.global.UsedMarketURL;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 文件上传工具类
 * Created by XT on 2016/10/7.
 */

public class UploadUtils {
    /**
     * 保存用户头像到服务器上
     */
    public static void uploadHead(File headFile) {
        if (headFile != null) {
            RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/UploadHeadServlet");    // 网址
            params.addBodyParameter("img", headFile);
            params.addBodyParameter("msg", "hello");
            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    UIUtils.toast(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    UIUtils.toast("上传文件网络出错啦~");
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }
}
