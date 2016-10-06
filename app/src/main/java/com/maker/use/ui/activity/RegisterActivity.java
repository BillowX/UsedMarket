package com.maker.use.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.maker.use.utils.FileUtil;
import com.maker.use.utils.UIUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

/**
 * 注册页面
 * Created by XT on 2016/10/5.
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    private final int CHOICE_HEAD_DIALOG = 0x1;

    @ViewInject(R.id.bt_register)
    Button bt_register;
    @ViewInject(R.id.bt_editHead)
    Button bt_editHead;
    @ViewInject(R.id.iv_head)
    ImageView iv_head;
    @ViewInject(R.id.et_username)
    EditText et_username;
    @ViewInject(R.id.et_password)
    EditText et_password;
    @ViewInject(R.id.rb_man)
    RadioButton rb_man;
    @ViewInject(R.id.rb_woman)
    RadioButton rb_woman;

    private Uri fileUri = null;
    private String headPath;
    private File mHeadFile;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        bt_editHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = et_username.getText().toString().trim();
                if (TextUtils.isEmpty(mUsername)) {
                    UIUtils.toast("先输入用户名，才能编辑头像！");
                    return;
                }

                showDialog(CHOICE_HEAD_DIALOG);
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //昵称、密码这两项时必填的，检查这两项
                mUsername = et_username.getText().toString().trim();
                mPassword = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(mUsername) && TextUtils.isEmpty(mPassword)) {
                    UIUtils.toast("昵称和密码不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(mUsername)) {
                    UIUtils.toast("昵称不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    UIUtils.toast("密码不能为空！");
                    return;
                }

                String sex = "woman";
                if (rb_man.isChecked())
                    sex = "man";
                final String finalSex = sex;

                RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/RegisterServlet");    // 网址
                params.addBodyParameter("username", mUsername);    // 参数1（post方式用addBodyParameter）
                params.addBodyParameter("password", mPassword);    // 参数2（post方式用addBodyParameter）
                params.addBodyParameter("sex", finalSex);    // 参数3（post方式用addBodyParameter）
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(final String result) {
                        UIUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                UIUtils.toast(result);
                            }
                        });
                        if ("注册成功".equals(result)) {
                            //保存用户头像
                            saveHead();

                            UIUtils.getContext().startActivity((new Intent(UIUtils.getContext(), LoginActivity.class)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
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

                    }
                });

               /* new Thread() {
                    @Override
                    public void run() {
                        String path = UsedMarketURL.server_heart + "/servlet/RegisterServlet";
                        URL url;
                        try {
                            url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setConnectTimeout(5000);
                            conn.setReadTimeout(5000);

                            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                            String info = "username=" + URLEncoder.encode(username) + "&password=" + password + "&sex=" + URLEncoder.encode(finalSex);
                            byte[] Info = info.getBytes();
                            conn.setRequestProperty("Content-Length", Info.length + "");

                            conn.setDoOutput(true);
                            OutputStream os = conn.getOutputStream();
                            os.write(Info);

                            if (conn.getResponseCode() == 200) {
                                InputStream is = conn.getInputStream();
                                final String text = InputUtils.getTextFromStream(is);
                                UIUtils.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        UIUtils.toast(text);
                                    }
                                });
                                if ("注册成功".equals(text)) {
                                    UIUtils.getContext().startActivity((new Intent(UIUtils.getContext(), LoginActivity.class)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    finish();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();*/
            }
        });

    }

    /**
     * 保存用户头像到服务器上
     */
    private void saveHead() {
        if (mHeadFile != null) {
            RequestParams params = new RequestParams(UsedMarketURL.server_heart + "/servlet/UploadServlet");    // 网址
            params.addBodyParameter("img", mHeadFile);
            params.addBodyParameter("msg", "hello");
            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    UIUtils.toast(result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    UIUtils.toast("上传头像网络出错啦~");
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

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case CHOICE_HEAD_DIALOG: {
                builder.setTitle("选择头像来源")
                        .setItems(new String[]{"相册", "拍照"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    //跳转到图片浏览器的应用，选取要发送的图片
                                    Intent i = new Intent();
                                    i.setType("image/*");
                                    i.putExtra("return-data", true);
                                    i.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(i, Activity.DEFAULT_KEYS_SHORTCUT);
                                } else if (which == 1) { //拍照
                                    Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
                                    mHeadFile = FileUtil.createHeadFile(mUsername + "_head");

                                    fileUri = Uri.fromFile(mHeadFile);
                                    it.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                    startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
                                }
                            }
                        });
            }
            break;
        }
        return builder.create();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        //通过“图片浏览器”获得自己的头像图片
        if (requestCode == Activity.DEFAULT_KEYS_SHORTCUT) {
            Uri uri = data.getData();
            mHeadFile = FileUtil.createHeadFile(mUsername + "_head");
            headPath = mHeadFile.getAbsolutePath();
            boolean result = FileUtil.writeFile(getContentResolver(), mHeadFile, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(mHeadFile.getAbsolutePath());
            if (bitmap != null) {
                iv_head.setImageBitmap(bitmap);
            }
        }
        //通过手机的拍照功能，获得自己的头像图片
        else if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
            headPath = fileUri.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(headPath);
            if (bitmap != null) {
                iv_head.setImageBitmap(bitmap);
            }
        }
    }

}
