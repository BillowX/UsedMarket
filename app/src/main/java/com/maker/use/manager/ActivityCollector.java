package com.maker.use.manager;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.maker.use.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理器
 * Created by XT on 2016/10/6.
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        Glide.get(UIUtils.getContext()).clearMemory();//清理内存缓存  可以在UI主线程中进行
    }

}
