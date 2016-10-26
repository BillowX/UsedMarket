package com.maker.use.manager;

import android.app.Activity;

import com.maker.use.utils.GlideUtils;

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
        GlideUtils.clearMemory();
    }

}
