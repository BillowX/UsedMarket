package com.maker.use.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.maker.use.R;

import io.rong.imlib.model.Conversation;

/**
 * 聊天页面
 * Created by XISEVEN on 2016/10/17.
 */
public class ConversationActivity extends FragmentActivity {

    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mTargetId = getIntent().getData().getQueryParameter("title");
        try {
            getActionBar().setTitle(mTargetId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

}
