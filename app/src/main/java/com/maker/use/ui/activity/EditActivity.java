package com.maker.use.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.utils.KeyBoardUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 编辑页面
 * Created by XT on 2016/11/3.
 */
@ContentView(R.layout.activity_edit)
public class EditActivity extends BaseActivity {

    @ViewInject(R.id.ibt_submit)
    ImageButton ibt_submit;
    @ViewInject(R.id.toolbar)
    Toolbar toolbar;
    @ViewInject(R.id.et_center)
    EditText et_center;
    @ViewInject(R.id.tv_num)
    TextView tv_num;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_center.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                int lastNum = 150 - length;
                tv_num.setText(lastNum + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ibt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeSoftKeyboard(EditActivity.this);
                Intent intent = new Intent();
                intent.putExtra("center",et_center.getText().toString());
                // 返回intent
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
