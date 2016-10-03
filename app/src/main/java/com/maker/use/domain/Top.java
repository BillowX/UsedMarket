package com.maker.use.domain;

import java.util.ArrayList;

/**
 * 一周美图TOP10的数据bean
 * Created by XT on 2016/9/27.
 */

public class Top {
    public ArrayList<img> imgs;

    public class img {
        public String title;
        public String imgUrl;

        @Override
        public String toString() {
            return title;
        }
    }
}
