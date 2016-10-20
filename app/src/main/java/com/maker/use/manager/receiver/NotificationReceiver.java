package com.maker.use.manager.receiver;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by XISEVEN on 2016/10/18.
 */

public class NotificationReceiver extends PushMessageReceiver {

    /**
     * 用来接收服务器发来的通知栏消息
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }

    /**
     * 是在用户点击通知栏消息时触发
     * @param context
     * @param pushNotificationMessage
     * @return
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
}
