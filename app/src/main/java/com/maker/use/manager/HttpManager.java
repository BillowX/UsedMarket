package com.maker.use.manager;

import android.content.Context;
import android.util.Log;

import com.maker.use.utils.GsonUtils;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * 网络请求封装库
 * Created by XT on 2016/11/8.
 */

public class HttpManager {

    private static HttpManager instance;
    private final Context context;

    private HttpManager(Context context) {
        this.context = context;
    }

    public static HttpManager getInstance(Context context) {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 发送网络请求
     *
     * @param url
     * @param paramters
     * @param callback
     */
    public void sendRequest(String url, Map<String, String> paramters, final objectCallback callback) {
        callback.onBegin();
        //请求网址
        RequestParams requestParams = new RequestParams(url);
        //添加请求参数
        if (paramters != null) {
            for (Map.Entry<String, String> me : paramters.entrySet()) {
                requestParams.addBodyParameter(me.getKey(), me.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("HttpRequest:success->", result);

                // TODO: 2016/11/8  让调用者知道拿到对象了
                if (callback != null) {
                    //解析成对象
                    Object object = GsonUtils.getGson().fromJson(result, callback.getDataClass());
                    callback.onSuccess(object);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                UIUtils.toast("网络出错啦~");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                callback.onFinished();
            }
        });

        // cancelable.cancel(); // 取消
    }

    //网络请求回调接口
    public abstract class objectCallback<T> {
        private final Class<T> clazz;

        @SuppressWarnings("unchecked")
        public objectCallback() {
            //获取到参数泛型的类
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            //只用到一个参数，所以用第0个
            clazz = (Class<T>) type.getActualTypeArguments()[0];
        }

        /**
         * 获取数据类型
         * @return
         */
        public Class<T> getDataClass() {
            return clazz;
        }

        public abstract void onBegin();//开始任务

        public abstract void onSuccess(T data);//成功返回

        public abstract void onFinished();//任务结束
    }
}
