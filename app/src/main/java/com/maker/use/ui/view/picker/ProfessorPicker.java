package com.maker.use.ui.view.picker;

import android.app.Activity;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by Administrator on 2016/10/27.
 */

public class ProfessorPicker extends OptionPicker {
    /**
     * Instantiates a new Constellation picker.
     *
     * @param activity the activity
     */
    public ProfessorPicker(Activity activity) {
        super(activity, new String[]{
                "A",
                "B",
                "AB",
                "O",
                "UnKnow"
        });
        setLabel("型");
    }


}
