package com.maker.use.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.maker.use.R;
import com.maker.use.ui.view.flyView.ShakeListener;
import com.maker.use.ui.view.flyView.StellarMap;
import com.maker.use.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Random;

/**
 * 搜索页面
 * Created by XT on 2016/10/12.
 */
@ContentView(R.layout.activity_search)
public class SearchActivity extends BaseActivity {

    @ViewInject(R.id.fl_recommend)
    FrameLayout fl_recommend;

    private ArrayList<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        //使用自定义的控件StellarMap
        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());
        stellarMap.setAdapter(new MyStellarMapAdapter());
        // 随机方式, 将控件划分为9行6列的的格子, 然后在格子中随机展示
        stellarMap.setRegularity(6, 9);
        // 设置内边距10dp
        int padding = UIUtils.dip2px(10);
        stellarMap.setInnerPadding(padding, padding, padding, padding);

        // 设置默认页面, 第一组数据
        stellarMap.setGroup(0, true);

        //添加一个手机晃动监听
        ShakeListener shake = new ShakeListener(UIUtils.getContext());
        shake.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake() {
                // 跳到下一页数据
                stellarMap.zoomIn();
            }
        });
        fl_recommend.addView(stellarMap);
    }

    class MyStellarMapAdapter implements StellarMap.Adapter {
        //分组数
        @Override
        public int getGroupCount() {
            return 2;
        }

        //返回某组的item个数
        @Override
        public int getCount(int group) {
            int count = mData.size() / getGroupCount();
            if (group == getGroupCount() - 1) {
                //除不尽的数加在最后一页显示
                count += mData.size() % getGroupCount();
            }
            return count;
        }

        //初始化布局
        @Override
        public View getView(int group, int position, View convertView) {
            //因为每次Position都会从0开始计数，所以根据前几组的数据加起来，才能确定当前的角标位置
            position += group * getCount(group - 1);

            final String keyword = mData.get(position);
            TextView textView = new TextView(UIUtils.getContext());
            textView.setText(keyword);
            //设置随机大小和随机颜色
            Random random = new Random();
            //设置单位为sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16 + random.nextInt(10));
            textView.setTextColor(Color.rgb(30 + random.nextInt(200), 30 + random.nextInt(200), 30 + random.nextInt(200)));
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    UIUtils.toast(keyword);
                }
            });

            return textView;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            //isZoomIn为TRUE时，说明向下滑动时
            if (isZoomIn) {
                // 往下滑加载上一页
                if (group > 0) {
                    group--;
                } else {
                    // 跳到最后一页
                    group = getGroupCount() - 1;
                }
            } else {
                // 往上滑加载下一页
                if (group < getGroupCount() - 1) {
                    group++;
                } else {
                    // 跳到第一页
                    group = 0;
                }
            }
            return group;
        }
    }
}
