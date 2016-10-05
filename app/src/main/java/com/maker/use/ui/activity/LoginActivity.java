package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.InputUtils;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 登陆界面
 * Created by XT on 2016/10/4.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.et_username)
    EditText et_username;
    @ViewInject(R.id.et_password)
    EditText et_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void login(View view) {
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();

        Thread t = new Thread() {
            public void run() {
                String path = UsedMarketURL.server_heart + "/servlet/LoginServlet?username=" + URLEncoder.encode(username) + "&password=" + password;

                URL url;
                try {
                    url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        final String text = InputUtils.getTextFromStream(is);
                        UIUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                UIUtils.toast(text);
                            }
                        });
                        if ("登陆成功！".equals(text)){
                            Intent intent = new Intent(UIUtils.getContext(), MainActivity.class);
                            intent.putExtra("info","login");
                            intent.putExtra("username",username);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            UIUtils.getContext().startActivity(intent);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void register(View view) {
        startActivity(new Intent(UIUtils.getContext(), RegisterActivity.class));
    }
}
