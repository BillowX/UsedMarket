package com.maker.use.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.maker.use.manager.CustomUserProvider;

import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;

/**
 * Created by XISEVEN on 2016/10/14.
 */

public class ChatUtils {
    /**
     * 打开对话页面
     *
     * @param context
     * @param mUersId 我的UersId
     * @param yUersId 对方的UersId
     */

    public static void openChat(final Context context, String mUersId, final String yUersId) {

        LCChatKit.getInstance().open(mUersId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    Intent intent = new Intent(context, LCIMConversationActivity.class);
                    intent.putExtra(LCIMConstants.PEER_ID, yUersId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 注册对话联系人
     *
     * @param userId    用户id
     * @param userName  用户名
     * @param avatarUrl 用户头像url
     */
    public static void registerChat(String userId, String userName, String avatarUrl) {
        List<LCChatKitUser> userList = CustomUserProvider.getInstance().getAllUsers();
        userList.add(new LCChatKitUser(userId, userName, avatarUrl));

    }
}
