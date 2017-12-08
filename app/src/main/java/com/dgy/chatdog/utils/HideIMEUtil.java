package com.dgy.chatdog.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiaoMan on 2016/11/22.
 * Email:1635401972@qq.com
 * Details:
 */
public class HideIMEUtil {
    public static void wrap(Activity activity) {
        ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        wrap(contentParent);
    }

    public static void wrap(Fragment fragment) {
        ViewGroup contentParent = (ViewGroup) fragment.getView().getParent();
        wrap(contentParent);
    }

    public static void wrap(ViewGroup contentParent) {
        View content = contentParent.getChildAt(0);
        contentParent.removeView(content);

        ViewGroup.LayoutParams p = content.getLayoutParams();
        AutoHideIMEFrameLayout layout = new AutoHideIMEFrameLayout(content.getContext());
        layout.addView(content);

        contentParent.addView(layout, new ViewGroup.LayoutParams(p.width, p.height));
    }
}