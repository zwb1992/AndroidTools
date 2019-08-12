package com.zwb.appmanager.jv;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

/**
 * @ author : zhouweibin
 * @ time: 2019/8/12 19:11.
 * @ desc:
 **/
public class AppManager {
    private AppManager() {
    }

    private static AppManager INSTANCE;

    public AppManager create() {
        if (INSTANCE == null) {
            synchronized (AppManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppManager();
                }
            }
        }
        return INSTANCE;
    }

    private Application app;

    public Application getApplication() {
        if (app != null) {
            return app;
        }
        try {
            return (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private int appCount = 0;

    /**
     * 是否处于前台
     */
    boolean isForeground = false;

    public LinkedList<Activity> activities = new LinkedList();

    /**
     * 初始化
     */
    public void init(Application app) {
        this.app = app;
        app.registerActivityLifecycleCallbacks(new ActivityLifecycleDelegate());
    }

    class ActivityLifecycleDelegate implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
            activities.addLast(activity);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            appCount++;
            if (appCount > 0) {
                isForeground = true;
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
            sortActivities(activity);
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            appCount--;
            if (appCount == 0) {
                isForeground = false;
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            activities.remove(activity);
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }
    }

    /**
     * 重新给activity列表排序
     */
    private void sortActivities(Activity activity) {
        if (activities.contains(activity) && activities.getLast() != activity) {
            activities.remove(activity);
            activities.addLast(activity);
        }
    }

    /**
     * 获取栈顶的activity
     */
    public Activity getTopActivity() {
        if (activities.isEmpty()) {
            return null;
        }
        return activities.getLast();
    }

    public void finishAll() {
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }
}
